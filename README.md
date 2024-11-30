# PARQUE DE DIVERSIONES

## Descripción
Este proyecto lleva a cabo la simulación de un parque de atracciones y su interacción con sus visitantes.
Dicha simulación incluye:
* Aspectos de gestión de tiempo: un reloj implementado para simular el pasaje del tiempo en el parque.
* Personas representadas como objetos activos que recorren el parque y realizan actividades de una lista de estas.
* Actividades tales como: Juegos de realidad virtual, juegos con premios, un comedor, un tren y un shopping.
Los usuarios tienen la posibilidad de definir diferentes algunos aspectos de estas actividades antes de su ejecución.

## Ejecución del proyecto
1. Dentro de su IDE de preferencia, importar el código fuente del proyecto.
2. Ejecutar el programa posicionandose sobre la clase Main.java encontrada en el directorio /src/Main/Main.java.

## Interacción
1. El programa inicialmente solicitará al usuario si desea, o no, utilizar los valores por defecto del programa.
2. En caso de seleccionar Si, se le solicitará que ingrese nuevos valores para la simulación.

```yaml
Bienvenido al simulador de parque de diversiones.
Desea utilizar los valores por defecto? (1: Sí, 0: No)
Equipos de readlidad virtual: 1
Encargados de juegos: 3
Personas: 5
Molinetes: 3
```

3. La simulación va a ejecutarse hasta que todos los hilos Persona terminen sus actividades y se retiren del parque. En ese momento, el programa imprimirá un mensaje de terminación de la simulación e interrumpirá el hilo GestorTiempo.
