#!/bin/bash

# Server Setup Script for CSR Docker Deployment
# This script installs Docker and Docker Compose on the server

set -e

echo "🚀 CSR Server Setup Script"
echo "=========================="

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

print_status() {
    echo -e "${BLUE}$1${NC}"
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

# Check if running as root
if [[ $EUID -ne 0 ]]; then
   print_error "This script must be run as root"
   exit 1
fi

print_status "Step 1: Updating system packages..."
apt-get update
print_success "System packages updated"

print_status "Step 2: Installing prerequisites..."
apt-get install -y \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg \
    lsb-release
print_success "Prerequisites installed"

print_status "Step 3: Checking Docker installation..."
if command -v docker &> /dev/null; then
    print_success "Docker is already installed"
    docker --version
else
    print_status "Installing Docker..."
    
    # Add Docker's official GPG key
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
    
    # Add Docker repository
    echo \
      "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu \
      $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null
    
    # Install Docker
    apt-get update
    apt-get install -y docker-ce docker-ce-cli containerd.io
    
    # Start and enable Docker
    systemctl start docker
    systemctl enable docker
    
    print_success "Docker installed successfully"
    docker --version
fi

print_status "Step 4: Checking Docker Compose installation..."
if command -v docker-compose &> /dev/null; then
    print_success "Docker Compose is already installed"
    docker-compose --version
else
    print_status "Installing Docker Compose..."
    
    # Get latest version of Docker Compose
    COMPOSE_VERSION=$(curl -s https://api.github.com/repos/docker/compose/releases/latest | grep 'tag_name' | cut -d\" -f4)
    
    # Download and install Docker Compose
    curl -L "https://github.com/docker/compose/releases/download/${COMPOSE_VERSION}/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    chmod +x /usr/local/bin/docker-compose
    
    print_success "Docker Compose installed successfully"
    docker-compose --version
fi

print_status "Step 5: Creating deployment directory..."
DEPLOY_DIR="/opt/csr"
mkdir -p $DEPLOY_DIR
chown root:root $DEPLOY_DIR
chmod 755 $DEPLOY_DIR
print_success "Deployment directory created: $DEPLOY_DIR"

print_status "Step 6: Setting up firewall rules..."
# Allow SSH (port 22)
ufw allow 22/tcp

# Allow HTTP (port 80)
ufw allow 80/tcp

# Allow HTTPS (port 443)
ufw allow 443/tcp

# Allow application port (8080)
ufw allow 8080/tcp

# Allow MySQL port (3306) - only if needed for external access
# ufw allow 3306/tcp

print_success "Firewall rules configured"

print_status "Step 7: Testing Docker installation..."
if docker run --rm hello-world &> /dev/null; then
    print_success "Docker is working correctly"
else
    print_error "Docker test failed"
    exit 1
fi

print_status "Step 8: Creating Docker data directories..."
mkdir -p $DEPLOY_DIR/data/mysql
chown -R 999:999 $DEPLOY_DIR/data/mysql  # MySQL container runs as uid 999
print_success "Data directories created"

echo ""
echo "========================================="
echo -e "${GREEN}🎉 Server Setup Complete!${NC}"
echo "========================================="
echo -e "${BLUE}Docker Version:${NC} $(docker --version)"
echo -e "${BLUE}Docker Compose Version:${NC} $(docker-compose --version)"
echo -e "${BLUE}Deployment Directory:${NC} $DEPLOY_DIR"
echo ""
echo -e "${YELLOW}Next steps:${NC}"
echo "1. Run the docker-deploy.ps1 script from your local machine"
echo "2. Your application will be available at: http://$(curl -s ifconfig.me):8080"
echo ""
echo -e "${YELLOW}Useful commands:${NC}"
echo "- View running containers: docker ps"
echo "- View all containers: docker ps -a"
echo "- View Docker images: docker images"
echo "- Clean up unused images: docker system prune" 