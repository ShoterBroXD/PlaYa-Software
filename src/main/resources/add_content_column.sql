-- Script para agregar el campo 'content' a la tabla threads
-- Ejecutar en la base de datos si no quieres esperar al reinicio del backend

ALTER TABLE threads 
ADD COLUMN content TEXT;

-- Verificar que se agregó correctamente
SELECT column_name, data_type 
FROM information_schema.columns 
WHERE table_name = 'threads';
