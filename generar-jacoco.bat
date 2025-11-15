@echo off
echo ========================================
echo Generando reporte de cobertura JaCoCo
echo ========================================
echo.

cd /d "%~dp0"

echo [1/4] Limpiando compilaciones anteriores...
call mvnw.cmd clean
if errorlevel 1 (
    echo ERROR: Fallo al limpiar el proyecto
    pause
    exit /b 1
)

echo.
echo [2/4] Compilando el proyecto...
call mvnw.cmd compile test-compile
if errorlevel 1 (
    echo ERROR: Fallo al compilar el proyecto
    pause
    exit /b 1
)

echo.
echo [3/4] Ejecutando tests con JaCoCo...
call mvnw.cmd test
if errorlevel 1 (
    echo ADVERTENCIA: Algunos tests fallaron, pero continuaremos con el reporte
)

echo.
echo [4/4] Generando reporte HTML...
call mvnw.cmd jacoco:report

echo.
echo Verificando si el reporte se genero...
if exist "target\site\jacoco\index.html" (
    echo.
    echo ========================================
    echo EXITO: Reporte generado correctamente  
    echo ========================================
    echo.
    echo Ubicacion: target\site\jacoco\index.html
    echo.
    echo Abriendo reporte en el navegador...
    start "" "target\site\jacoco\index.html"
) else (
    echo.
    echo ========================================
    echo ERROR: No se genero el reporte
    echo ========================================
    echo.
    echo Buscando archivos de JaCoCo...
    if exist "target\jacoco.exec" (
        echo - jacoco.exec encontrado: SI
    ) else (
        echo - jacoco.exec encontrado: NO
        echo   PROBLEMA: JaCoCo no esta recopilando datos
    )
    echo.
    if exist "target\site" (
        echo - Directorio target\site: SI
        dir /s /b target\site
    ) else (
        echo - Directorio target\site: NO
    )
    echo.
    echo Posibles causas:
    echo - Los tests no se ejecutaron completamente
    echo - JaCoCo no esta configurado correctamente en pom.xml
    echo - Hay errores de compilacion
    echo.
)

echo.
pause
