# Docker CSR Project Deployment Script
# Author: Auto-generated Docker deployment script
# Description: Builds Docker images locally, uploads to server, and runs with Docker Compose

param(
    [string]$RemoteHost = "8.133.240.77",
    [string]$RemoteUser = "root",
    [string]$RemotePassword = "ocbcCSR$",
    [string]$RemotePath = "/opt/csr",
    [string]$ImageTag = "latest"
)

# Colors for output
function Write-ColorOutput {
    param([string]$Message, [string]$Color = "White")
    try {
        Write-Host $Message -ForegroundColor $Color
    } catch {
        # Fallback to plain text if color fails
        Write-Host $Message
    }
}

function Test-Command {
    param([string]$Command)
    try {
        Get-Command $Command -ErrorAction Stop | Out-Null
        return $true
    }
    catch {
        return $false
    }
}

# Script start
Write-ColorOutput "========================================" "Cyan"
Write-ColorOutput "CSR Docker Deployment Script" "Cyan"
Write-ColorOutput "========================================" "Cyan"
Write-ColorOutput "Target: ${RemoteUser}@${RemoteHost}:${RemotePath}" "Yellow"
Write-ColorOutput "Image Tag: $ImageTag" "Yellow"
Write-ColorOutput ""

# Check prerequisites
Write-ColorOutput "🔍 Step 1: Checking prerequisites..." "Cyan"

# Check Docker
if (-not (Test-Command "docker")) {
    Write-ColorOutput "Docker not found! Please install Docker Desktop" "Red"
    exit 1
}
Write-ColorOutput "Docker found" "Green"

# Check SCP
$ScpCommand = $null
if (Test-Command "scp") {
    $ScpCommand = "scp"
    Write-ColorOutput "Using OpenSSH SCP" "Green"
} elseif (Test-Command "pscp") {
    $ScpCommand = "pscp"
    Write-ColorOutput "Using PuTTY SCP (pscp)" "Green"
} else {
    Write-ColorOutput "SCP not found! Please install OpenSSH or PuTTY" "Red"
    exit 1
}

# Check SSH
$SshCommand = $null
if (Test-Command "ssh") {
    $SshCommand = "ssh"
    Write-ColorOutput "Using OpenSSH" "Green"
} elseif (Test-Command "plink") {
    $SshCommand = "plink"
    Write-ColorOutput "Using PuTTY plink" "Green"
} else {
    Write-ColorOutput "SSH not found! Please install OpenSSH or PuTTY" "Red"
    exit 1
}

# Step 2: Build Docker image
Write-ColorOutput "🐳 Step 2: Building Docker image..." "Cyan"
$ImageName = "csr-app:$ImageTag"

try {
    Write-ColorOutput "Building image: $ImageName" "Yellow"
    $output = docker build -t $ImageName . 2>&1
    
    if ($LASTEXITCODE -eq 0) {
        Write-ColorOutput "✓ Docker image built successfully!" "Green"
    } else {
        Write-ColorOutput "Docker build failed!" "Red"
        Write-ColorOutput "Build output:" "Red"
        $output | Write-Host
        exit 1
    }
} catch {
    Write-ColorOutput "Error building Docker image: $($_.Exception.Message)" "Red"
    exit 1
}

# Step 3: Save Docker image to tar file
Write-ColorOutput "💾 Step 3: Saving Docker image..." "Cyan"
$TarFile = "csr-app-$ImageTag.tar"

try {
    Write-ColorOutput "Saving image to: $TarFile" "Yellow"
    $output = docker save -o $TarFile $ImageName 2>&1
    
    if ($LASTEXITCODE -eq 0) {
        $TarSize = (Get-Item $TarFile).Length / 1MB
        Write-ColorOutput "Image saved successfully! (Size: $([math]::Round($TarSize, 2)) MB)" "Green"
    } else {
        Write-ColorOutput "Failed to save Docker image!" "Red"
        Write-ColorOutput "Output: $output" "Red"
        exit 1
    }
} catch {
    Write-ColorOutput "Error saving Docker image: $($_.Exception.Message)" "Red"
    exit 1
}

# Step 4: Prepare remote directory
Write-ColorOutput "🔧 Step 4: Preparing remote directory..." "Cyan"
try {
    Write-ColorOutput "Creating directory $RemotePath on $RemoteHost" "Yellow"
    $mkdirCommand = "mkdir -p $RemotePath"
    if ($SshCommand -eq "ssh") {
        $output = & ssh -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null "${RemoteUser}@${RemoteHost}" $mkdirCommand 2>&1
    } else {
        $output = & plink -pw $RemotePassword -batch "${RemoteUser}@${RemoteHost}" $mkdirCommand 2>&1
    }

    if ($LASTEXITCODE -eq 0) {
        Write-ColorOutput "✓ Remote directory prepared successfully!" "Green"
    } else {
        Write-ColorOutput "✗ Failed to create remote directory!" "Red"
        Write-ColorOutput "Output: $output" "Red"
        exit 1
    }
} catch {
    Write-ColorOutput "✗ Error preparing remote directory: $($_.Exception.Message)" "Red"
    exit 1
}

# Step 5: Upload files to server
Write-ColorOutput "📤 Step 5: Uploading files to server..." "Cyan"

$RemoteTarPath = "$RemotePath/$TarFile"
$RemoteComposePath = "$RemotePath/compose.yaml"
$RemoteEnvPath = "$RemotePath/.env"

# Files to upload
$FilesToUpload = @(
    @{ Local = $TarFile; Remote = $RemoteTarPath; Description = "Docker image" },
    @{ Local = "compose.yaml"; Remote = $RemoteComposePath; Description = "Docker Compose file" },
    @{ Local = ".env"; Remote = $RemoteEnvPath; Description = "Environment file" }
)

try {
    foreach ($File in $FilesToUpload) {
        if (Test-Path $File.Local) {
            Write-ColorOutput "Uploading $($File.Description): $($File.Local)" "Yellow"
            
            if ($ScpCommand -eq "scp") {
                $output = & scp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null $File.Local "${RemoteUser}@${RemoteHost}:$($File.Remote)" 2>&1
            } else {
                $output = & pscp -pw $RemotePassword -batch $File.Local "${RemoteUser}@${RemoteHost}:$($File.Remote)" 2>&1
            }
            
            if ($LASTEXITCODE -eq 0) {
                Write-ColorOutput "$($File.Description) uploaded successfully!" "Green"
            } else {
                Write-ColorOutput "Failed to upload $($File.Description)!" "Red"
                Write-ColorOutput "Output: $output" "Red"
                exit 1
            }
        } else {
            Write-ColorOutput "$($File.Local) not found, skipping..." "Yellow"
        }
    }
} catch {
    Write-ColorOutput "Error during upload: $($_.Exception.Message)" "Red"
    exit 1
}

# Step 6: Setup and run on server
Write-ColorOutput "🚀 Step 6: Setting up and running on server..." "Cyan"

$RemoteCommands = @"
# Change to deployment directory
cd $RemotePath

# Load Docker image
echo "Loading Docker image..."
docker load -i $TarFile

# Stop existing containers
echo "Stopping existing containers..."
docker-compose down 2>/dev/null || true

# Remove old image if exists
echo "Cleaning up old images..."
docker image prune -f

# Start services
echo "Starting services with Docker Compose..."
docker-compose up -d

# Show status
echo "Container status:"
docker-compose ps

echo "Deployment completed successfully!"
echo "Application should be available at: http://$RemoteHost:8080"
"@

try {
    Write-ColorOutput "Executing remote commands..." "Yellow"
    
    if ($SshCommand -eq "ssh") {
        $output = $RemoteCommands | & ssh -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null "${RemoteUser}@${RemoteHost}" 'bash -s' 2>&1
    } else {
        # Using plink
        $output = $RemoteCommands | & plink -batch -pw $RemotePassword "${RemoteUser}@${RemoteHost}" 'bash -s' 2>&1
    }
    
    if ($LASTEXITCODE -eq 0) {
        Write-ColorOutput "Remote setup completed successfully!" "Green"
        Write-ColorOutput "Remote output:" "Cyan"
        $output | Write-Host
    } else {
        Write-ColorOutput "Remote setup completed with warnings" "Yellow"
        Write-ColorOutput "Remote output:" "Yellow"
        $output | Write-Host
    }
} catch {
    Write-ColorOutput "Error executing remote commands: $($_.Exception.Message)" "Red"
    exit 1
}

# Step 7: Cleanup local files
Write-ColorOutput "🧹 Step 7: Cleaning up local files..." "Cyan"
try {
    Remove-Item $TarFile -Force
    Write-ColorOutput "Local tar file cleaned up" "Green"
} catch {
    Write-ColorOutput "Could not remove local tar file: $TarFile" "Yellow"
}

# Final status
Write-ColorOutput "" 
Write-ColorOutput "========================================" "Cyan"
Write-ColorOutput "🎉 Deployment Complete!" "Green"
Write-ColorOutput "========================================" "Cyan"
Write-ColorOutput "Application URL: http://$RemoteHost:8080" "Green"
Write-ColorOutput "Server Path: $RemotePath" "Yellow"
Write-ColorOutput "" 
Write-ColorOutput "Useful commands for server management:" "Cyan"
Write-ColorOutput "View logs: ssh $RemoteUser@$RemoteHost 'cd $RemotePath && docker-compose logs -f'" "Yellow"
Write-ColorOutput "Stop services: ssh $RemoteUser@$RemoteHost 'cd $RemotePath && docker-compose down'" "Yellow"
Write-ColorOutput "Restart services: ssh $RemoteUser@$RemoteHost 'cd $RemotePath && docker-compose restart'" "Yellow"
Write-ColorOutput "Check status: ssh $RemoteUser@$RemoteHost 'cd $RemotePath && docker-compose ps'" "Yellow" 
