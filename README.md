# FideBank - Avance 1

Proyecto final de Programación Cliente/Servidor Concurrente (SC-303), Universidad Fidélitas.

## Ejecutar
1. Abrir la carpeta `FideBank` en Visual Studio Code (con Extension Pack for Java) o en NetBeans.
2. Ejecutar `src/fidebank/Main.java`.

Desde la terminal:
```
javac -d bin src/fidebank/*.java
java -cp bin fidebank.Main
```

## Datos de prueba
| Cliente | Cédula | PIN | Cuentas |
|---|---|---|---|
| Ana Solís Vargas | 114560789 | 4826 | FB-1001 (ahorro), FB-1002 (corriente) |
| Carlos Jiménez Rojas | 205550111 | 7391 | FB-1003 (ahorro) |

Los comprobantes impresos se guardan como `comprobante_N.txt` en la carpeta desde donde se ejecuta el programa.
