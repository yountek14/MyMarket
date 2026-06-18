@echo off
echo Iniciando todos los microservicios de MyMarket...

start "MS Alertas" cmd /k "cd ms-alertas && mvn spring-boot:run"
start "MS Empleados" cmd /k "cd ms-empleados && mvn spring-boot:run"
start "MS Inventario" cmd /k "cd ms-inventario && mvn spring-boot:run"
start "MS Pedidos" cmd /k "cd ms-pedidos && mvn spring-boot:run"
start "MS Precios" cmd /k "cd ms-precios && mvn spring-boot:run"
start "MS Productos" cmd /k "cd ms-productos && mvn spring-boot:run"
start "MS Proveedores" cmd /k "cd ms-proveedores && mvn spring-boot:run"
start "MS Reportes" cmd /k "cd ms-reportes && mvn spring-boot:run"
start "MS Usuario" cmd /k "cd ms-usuario && mvn spring-boot:run"
start "MS Ventas" cmd /k "cd ms-ventas && mvn spring-boot:run"

echo Todos los microservicios han sido lanzados en ventanas separadas.
echo Puedes cerrar cada ventana individualmente para detener el servicio correspondiente.
