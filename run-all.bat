@echo off
echo Iniciando Eureka Server...
start "Eureka Server" cmd /c "cd eureka-server && mvnw.cmd spring-boot:run"
timeout /t 5

echo Iniciando Main Producto...
start "Main Producto" cmd /c "cd MainProducto && mvnw.cmd spring-boot:run"

echo Iniciando Categorias...
start "Categorias" cmd /c "cd categorias && mvnw.cmd spring-boot:run"

echo Iniciando Enlaces...
start "Enlaces" cmd /c "cd enlaces && mvnw.cmd spring-boot:run"

timeout /t 10
echo Iniciando API Gateway...
start "API Gateway" cmd /c "cd api-gateway && mvnw.cmd spring-boot:run"

echo Todos los servicios estan inicializando en nuevas ventanas.
pause
