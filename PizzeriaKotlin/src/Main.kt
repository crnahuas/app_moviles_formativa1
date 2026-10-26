fun main() {
    println("=== Pizzería ===")

    // Primero registramos la pizza y cuántas hay disponibles.
    println("\n--- Pizza ---")
    val nombrePizza: String = leerTexto("Nombre de la pizza: ", "Pizza de la casa")
    val precioUnitario: Double = leerPrecio() ?: return
    var pizzasDisponibles: Int =
        leerEntero("Cantidad de pizzas disponibles: ", 0) ?: return
    val repartidorDisponible: Boolean = true
    val nombreRepartidor: String = "Camila"

    // Registramos los datos del cliente para la entrega.
    println("\n--- Datos del Cliente ---")
    val nombre: String = leerTextoObligatorio(
        "¿A nombre de quién es el pedido? ",
        "Ingresa tu nombre para continuar."
    ) ?: return
    val direccion: String = leerTextoObligatorio(
        "¿Dónde lo entregamos? ",
        "Ingresa la dirección de entrega para continuar."
    ) ?: return

    print("Teléfono de contacto (por ejemplo, +56912345678): ")
    val telefonoIngresado: String = readlnOrNull()?.trim() ?: ""
    val telefono: String =
        if (telefonoIngresado.matches(Regex("\\+?[0-9]{9,15}"))) {
            println("Teléfono registrado.")
            telefonoIngresado
        } else {
            println("Ese teléfono no parece estar bien. Por ahora, seguiremos sin un número de contacto.")
            "Sin teléfono"
        }

    // Creamos el pedido con la cantidad que solicita el cliente.
    println("\n--- Pedido ---")
    val idPedido: Int = 1
    var estadoPedido: String = "Pendiente"
    println("Pedido #$idPedido: $estadoPedido")
    println("Pizza: $nombrePizza | Precio: $precioUnitario pesos cada una")
    val cantidadPizzas: Int = leerEntero("¿Cuántas pizzas vas a pedir? ", 1) ?: return

    // Revisamos si hay suficientes pizzas para el pedido.
    val hayStock: Boolean = pizzasDisponibles >= cantidadPizzas

    // Calculamos el subtotal si hay suficientes pizzas.
    val subtotal: Double = if (hayStock) {
        precioUnitario * cantidadPizzas
    } else {
        0.0
    }

    // Si el cálculo supera el límite, se detiene el pedido.
    if (!subtotal.isFinite()) {
        println("Revisa el precio y la cantidad: el total es demasiado grande y no pudimos confirmar el pedido.")
        return
    }

    // Los pedidos de más de tres pizzas tienen un 10% de descuento.
    val descuento: Double = if (hayStock && cantidadPizzas > 3) {
        subtotal * 0.10
    } else {
        0.0
    }
    val totalPedido: Double = subtotal - descuento

    // Al confirmar el pedido, descontamos las pizzas vendidas.
    estadoPedido = if (hayStock) {
        pizzasDisponibles -= cantidadPizzas
        println("\nPedido confirmado.")
        "En preparación"
    } else {
        println("\nNo hay suficiente stock disponible para este pedido.")
        "No confirmado por falta de stock"
    }

    // Asignamos un repartidor si el pedido está confirmado y hay uno disponible.
    val repartidorAsignado: String = if (hayStock && repartidorDisponible) {
        nombreRepartidor
    } else {
        "Sin asignar"
    }

    // Mostramos el resumen tanto si el pedido se confirmó o se rechazó.
    println("\n=== Resumen de tu pedido ===")
    println("Pedido #$idPedido")
    println("A nombre de: $nombre")
    println("Dirección de entrega: $direccion")
    println("Teléfono: $telefono")
    println("Pizza: $nombrePizza")
    println("Precio por pizza: $precioUnitario pesos")
    println("Cantidad de pizzas: $cantidadPizzas")
    if (hayStock) {
        println("Subtotal: $subtotal pesos")
        println("Descuento: $descuento pesos")
    }
    println("Total a pagar: $totalPedido pesos")
    println("Estado: $estadoPedido")
    println("Repartidor asignado: $repartidorAsignado")
    println("Pizzas disponibles: $pizzasDisponibles")
    if (!hayStock) {
        println("Pedido no confirmado. Stock insuficiente.")
    }
}

// Si no se ingresa un texto, usamos el valor predeterminado.
fun leerTexto(mensaje: String, valorPredeterminado: String): String {
    print(mensaje)
    val entrada: String = readlnOrNull()?.trim() ?: ""
    return if (entrada.isBlank()) {
        println("No escribiste nada. Usaremos \"$valorPredeterminado\".")
        valorPredeterminado
    } else {
        println("Dato registrado.")
        entrada
    }
}

// El nombre y la dirección son necesarios para poder entregar el pedido.
fun leerTextoObligatorio(mensaje: String, mensajeError: String): String? {
    while (true) {
        print(mensaje)
        val entrada: String? = readlnOrNull()?.trim()
        if (entrada == null) {
            println("\nNos faltan datos para continuar. El pedido quedó sin confirmar.")
            return null
        }
        if (entrada.isNotBlank()) {
            println("Dato registrado.")
            return entrada
        }
        println(mensajeError)
    }
}

// Volvemos a preguntar si el número no es válido.
fun leerEntero(mensaje: String, minimo: Int): Int? {
    while (true) {
        print(mensaje)
        val entrada: String? = readlnOrNull()
        if (entrada == null) {
            println("\nNos faltan datos para continuar. El pedido quedó sin confirmar.")
            return null
        }
        val numero: Int? = entrada.trim().toIntOrNull()
        if (numero != null && numero >= minimo) {
            println("Cantidad registrada.")
            return numero
        }
        println("Escribe una cantidad de $minimo o más, sin decimales.")
    }
}

// El precio debe ser un número válido y mayor que cero.
fun leerPrecio(): Double? {
    while (true) {
        print("Precio por pizza en pesos (por ejemplo, 8500): ")
        val entrada: String? = readlnOrNull()
        if (entrada == null) {
            println("\nNos faltan datos para continuar. El pedido quedó sin confirmar.")
            return null
        }
        val precio: Double? = entrada.trim().replace(',', '.').toDoubleOrNull()
        if (precio != null && precio.isFinite() && precio > 0.0) {
            println("Precio registrado.")
            return precio
        }
        println("Revisa el precio. Escribe un número mayor que cero, como 8500.")
    }
}
