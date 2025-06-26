# Local Docker Build and Run Script for CSR
# Use this to test your Docker setup locally before deploying to server

param(
    [switch]$Build,
    [switch]$Start,
    [switch]$Stop,
    [switch]$Restart,
    [switch]$Logs,
    [switch]$Clean,
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

function Show-Usage {
    Write-ColorOutput "CSR Local Docker Management Script" "Cyan"
    Write-ColorOutput "=================================" "Cyan"
    Write-ColorOutput ""
    Write-ColorOutput "Usage:" "Yellow"
    Write-ColorOutput "  .\docker-build-local.ps1 -Build          # Build Docker image" "White"
    Write-ColorOutput "  .\docker-build-local.ps1 -Start          # Start containers" "White"
    Write-ColorOutput "  .\docker-build-local.ps1 -Stop           # Stop containers" "White"
    Write-ColorOutput "  .\docker-build-local.ps1 -Restart        # Restart containers" "White"
    Write-ColorOutput "  .\docker-build-local.ps1 -Logs           # Show container logs" "White"
    Write-ColorOutput "  .\docker-build-local.ps1 -Clean          # Clean up containers and images" "White"
    Write-ColorOutput ""
    Write-ColorOutput "Examples:" "Yellow"
    Write-ColorOutput "  .\docker-build-local.ps1 -Build -Start   # Build and start" "White"
    Write-ColorOutput "  .\docker-build-local.ps1 -Build -ImageTag v1.0  # Build with custom tag" "White"
}

# Check if Docker is available
if (-not (Get-Command "docker" -ErrorAction SilentlyContinue)) {
    Write-ColorOutput "✗ Docker not found! Please install Docker Desktop" "Red"
    exit 1
}

# Check if .env file exists
if (-not (Test-Path ".env")) {
    Write-ColorOutput "⚠ .env file not found! Creating from template..." "Yellow"
    @"
# MySQL settings
MYSQL_ROOT_PASSWORD=iMd49mc4~*(>
MYSQL_DATABASE=csr
MYSQL_USER=csr
MYSQL_PASSWORD=6!<qp~4ehZ1u

# Spring Datasource settings
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/csr
SPRING_DATASOURCE_USERNAME=csr
SPRING_DATASOURCE_PASSWORD=6!<qp~4ehZ1u

# JWT settings
JWT_SECRET=please-change-this-to-a-secure-secret-for-local-testing
JWT_ACCESS_TOKEN_EXPIRATION=3600000
JWT_REFRESH_TOKEN_EXPIRATION=86400000
"@ | Out-File -FilePath ".env" -Encoding UTF8
    Write-ColorOutput "✓ .env file created. Please review and update if needed." "Green"
}

# Show usage if no parameters
if (-not ($Build -or $Start -or $Stop -or $Restart -or $Logs -or $Clean)) {
    Show-Usage
    exit 0
}

$ImageName = "csr-app:$ImageTag"

# Build image
if ($Build) {
    Write-ColorOutput "🐳 Building Docker image: $ImageName" "Cyan"
    try {
        $output = docker build -t $ImageName . 2>&1
        
        if ($LASTEXITCODE -eq 0) {
            Write-ColorOutput "✓ Docker image built successfully!" "Green"
        } else {
            Write-ColorOutput "✗ Docker build failed!" "Red"
            $output | Write-Host
            exit 1
        }
    } catch {
        Write-ColorOutput "✗ Error building Docker image: $($_.Exception.Message)" "Red"
        exit 1
    }
}

# Stop containers
if ($Stop -or $Restart) {
    Write-ColorOutput "🛑 Stopping containers..." "Cyan"
    docker-compose down
    if ($LASTEXITCODE -eq 0) {
        Write-ColorOutput "✓ Containers stopped" "Green"
    }
}

# Start containers
if ($Start -or $Restart) {
    Write-ColorOutput "🚀 Starting containers..." "Cyan"
    docker-compose up -d
    if ($LASTEXITCODE -eq 0) {
        Write-ColorOutput "✓ Containers started successfully!" "Green"
        Write-ColorOutput ""
        Write-ColorOutput "Application Status:" "Cyan"
        docker-compose ps
        Write-ColorOutput ""
        Write-ColorOutput "🌐 Application should be available at:" "Green"
        Write-ColorOutput "   http://localhost:8080" "Yellow"
        Write-ColorOutput ""
        Write-ColorOutput "💾 MySQL is available at:" "Green"
        Write-ColorOutput "   Host: localhost" "Yellow"
        Write-ColorOutput "   Port: 3306" "Yellow"
        Write-ColorOutput "   Database: csr" "Yellow"
        Write-ColorOutput "   Username: csr" "Yellow"
    } else {
        Write-ColorOutput "✗ Failed to start containers" "Red"
        exit 1
    }
}

# Show logs
if ($Logs) {
    Write-ColorOutput "📋 Container logs:" "Cyan"
    docker-compose logs -f
}

# Clean up
if ($Clean) {
    Write-ColorOutput "🧹 Cleaning up..." "Cyan"
    
    # Stop and remove containers
    docker-compose down
    
    # Remove images
    docker rmi $ImageName 2>$null
    docker rmi $(docker images -f "dangling=true" -q) 2>$null
    
    # Remove volumes (optional - uncomment if you want to reset database)
    # docker-compose down -v
    
    # Prune system
    docker system prune -f
    
    Write-ColorOutput "✓ Cleanup completed" "Green"
} 