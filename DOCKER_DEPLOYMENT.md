# CSR Docker Deployment Guide

This guide explains how to build, test, and deploy the CSR application using Docker containers.

## 📋 Prerequisites

### Local Machine Requirements
- **Docker Desktop** (for Windows/Mac) or **Docker Engine** (for Linux)
- **PowerShell** (Windows) or **Bash** (Linux/Mac)
- **SSH/SCP client** (OpenSSH or PuTTY)
- **Git** (for version control)

### Server Requirements
- **Ubuntu 18.04+** or **CentOS 7+**
- **Root access** or sudo privileges
- **Internet connection**
- **Ports 22, 80, 443, 8080** open in firewall

## 🚀 Quick Start

### 1. First-Time Server Setup

Before deploying, you need to set up Docker on your server:

```powershell
# Upload and run the server setup script
scp server-setup.sh root@8.133.240.77:/tmp/
ssh root@8.133.240.77 "chmod +x /tmp/server-setup.sh && /tmp/server-setup.sh"
```

### 2. Local Testing (Recommended)

Test your Docker setup locally before deploying:

```powershell
# Build and start containers locally
.\docker-build-local.ps1 -Build -Start

# View logs
.\docker-build-local.ps1 -Logs

# Stop containers
.\docker-build-local.ps1 -Stop
```

### 3. Deploy to Server

Once local testing is successful:

```powershell
# Deploy to server
.\docker-deploy.ps1
```

## 📁 File Structure

```
csr/
├── Dockerfile                 # Docker image definition
├── compose.yaml              # Docker Compose configuration
├── .env                      # Environment variables (create from template)
├── docker-deploy.ps1         # Main deployment script
├── docker-build-local.ps1    # Local testing script
├── server-setup.sh           # Server setup script
└── DOCKER_DEPLOYMENT.md      # This guide
```

## 🔧 Configuration Files

### Environment Variables (.env)

Create a `.env` file with the following variables:

```env
# MySQL settings
MYSQL_ROOT_PASSWORD=your-secure-root-password
MYSQL_DATABASE=csr
MYSQL_USER=csr
MYSQL_PASSWORD=your-secure-password

# Spring Datasource settings
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/csr
SPRING_DATASOURCE_USERNAME=csr
SPRING_DATASOURCE_PASSWORD=your-secure-password

# JWT settings
JWT_SECRET=your-very-secure-jwt-secret-key
JWT_ACCESS_TOKEN_EXPIRATION=3600000
JWT_REFRESH_TOKEN_EXPIRATION=86400000
```

### Docker Compose (compose.yaml)

The `compose.yaml` file defines two services:
- **mysql**: MySQL 8.0 database
- **csr-app**: Spring Boot application

## 📜 Available Scripts

### 1. docker-build-local.ps1

Local development and testing script.

**Usage:**
```powershell
# Build Docker image
.\docker-build-local.ps1 -Build

# Start containers
.\docker-build-local.ps1 -Start

# Build and start in one command
.\docker-build-local.ps1 -Build -Start

# Stop containers
.\docker-build-local.ps1 -Stop

# Restart containers
.\docker-build-local.ps1 -Restart

# View logs (follow mode)
.\docker-build-local.ps1 -Logs

# Clean up everything
.\docker-build-local.ps1 -Clean

# Build with custom tag
.\docker-build-local.ps1 -Build -ImageTag "v1.0"
```

### 2. docker-deploy.ps1

Production deployment script.

**Usage:**
```powershell
# Deploy with default settings
.\docker-deploy.ps1

# Deploy with custom parameters
.\docker-deploy.ps1 -RemoteHost "your-server-ip" -RemoteUser "root" -RemotePath "/opt/csr"

# Deploy with custom image tag
.\docker-deploy.ps1 -ImageTag "v1.0"
```

**What it does:**
1. Builds Docker image locally
2. Saves image as tar file
3. Uploads tar file, compose.yaml, and .env to server
4. Loads image on server
5. Starts containers with Docker Compose
6. Cleans up local tar file

### 3. server-setup.sh

Server preparation script (run once per server).

**What it installs:**
- Docker CE
- Docker Compose
- Required system packages
- Firewall rules
- Deployment directories

## 🔍 Troubleshooting

### Common Issues

#### 1. Docker Build Fails
```bash
# Check Docker is running
docker --version

# Check Dockerfile syntax
docker build --no-cache -t csr-app:test .
```

#### 2. Container Won't Start
```bash
# Check logs
docker-compose logs csr-app
docker-compose logs mysql

# Check container status
docker-compose ps
```

#### 3. Database Connection Issues
```bash
# Check MySQL is ready
docker-compose exec mysql mysql -u csr -p -e "SHOW DATABASES;"

# Check network connectivity
docker-compose exec csr-app ping mysql
```

#### 4. Port Already in Use
```bash
# Find what's using the port
netstat -tulpn | grep :8080

# Stop conflicting services
sudo systemctl stop apache2  # if Apache is running
```

### Useful Commands

#### Local Development
```powershell
# View running containers
docker ps

# Access application logs
docker-compose logs -f csr-app

# Access database
docker-compose exec mysql mysql -u csr -p

# Rebuild without cache
docker build --no-cache -t csr-app:latest .
```

#### Server Management
```bash
# SSH to server
ssh root@8.133.240.77

# Navigate to deployment directory
cd /opt/csr

# View container status
docker-compose ps

# View logs
docker-compose logs -f

# Restart services
docker-compose restart

# Stop services
docker-compose down

# Update and restart
docker-compose down && docker-compose up -d
```

## 🔒 Security Considerations

1. **Change Default Passwords**: Update all passwords in `.env` file
2. **Secure JWT Secret**: Use a strong, random JWT secret
3. **Firewall**: Ensure only necessary ports are open
4. **SSL/TLS**: Consider adding reverse proxy with SSL
5. **Database**: MySQL is not exposed externally by default
6. **Updates**: Regularly update Docker images and host system

## 🌐 Accessing the Application

After successful deployment:
- **Application**: http://your-server-ip:8080
- **Health Check**: http://your-server-ip:8080/actuator/health (if enabled)

## 📊 Monitoring

### View Application Logs
```bash
# Follow all logs
docker-compose logs -f

# Follow specific service
docker-compose logs -f csr-app
docker-compose logs -f mysql

# View last N lines
docker-compose logs --tail=100 csr-app
```

### Resource Usage
```bash
# Container resource usage
docker stats

# Disk usage
docker system df

# Clean up unused resources
docker system prune
```

## 🔄 Updates and Maintenance

### Updating the Application
1. Make code changes locally
2. Test with `docker-build-local.ps1 -Build -Start`
3. Deploy with `docker-deploy.ps1 -ImageTag "v1.1"`

### Database Backups
```bash
# Backup database
docker-compose exec mysql mysqldump -u csr -p csr > backup.sql

# Restore database
docker-compose exec -T mysql mysql -u csr -p csr < backup.sql
```

### Clean Up Old Images
```bash
# On server
docker image prune -a
docker system prune -a
```

## 📞 Support

If you encounter issues:
1. Check the logs: `docker-compose logs -f`
2. Verify configuration: `.env` file and `compose.yaml`
3. Test connectivity: `docker-compose exec csr-app ping mysql`
4. Check server resources: `docker stats`

## 📚 Additional Resources

- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Spring Boot Docker Guide](https://spring.io/guides/gs/spring-boot-docker/)
- [MySQL Docker Hub](https://hub.docker.com/_/mysql) 