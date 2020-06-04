package ar.com.ada.hoteltresvagos;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.hibernate.exception.ConstraintViolationException;

import ar.com.ada.hoteltresvagos.entities.*;
import ar.com.ada.hoteltresvagos.excepciones.*;
import ar.com.ada.hoteltresvagos.managers.*;

public class ABM {

    public static Scanner Teclado = new Scanner(System.in);

    protected HuespedManager ABMHuesped = new HuespedManager();

    public void iniciar() throws Exception {

        try {

            ABMHuesped.setup(); //del huespedManager

            printOpciones();

            int opcion = Teclado.nextInt();
            Teclado.nextLine();

            while (opcion > 0) {

                switch (opcion) {
                    case 1: // hacer reserva= crear huesped y reserva

                        try {
                            alta();
                        } catch (HuespedDNIException exdni) {
                            System.out.println("Error en el DNI. Indique uno valido");
                        }
                        break;

                    case 2: // modificar = reserva y huesped
                    modifica();    
                    baja();
                        break;

                    case 3: // consultar reserva o huesped
                    listar(); 
                        break;

                    default:
                        System.out.println("La opcion no es correcta.");
                        break;
                }

                printOpciones();

                opcion = Teclado.nextInt();
                Teclado.nextLine();
            }

            // Hago un safe exit del manager
            ABMHuesped.exit();

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Que lindo mi sistema,se rompio mi sistema");
            throw e;
        } finally {
            System.out.println("Saliendo del sistema, bye bye...");

        }

    }

    public void alta() throws Exception {
        Huesped huesped = new Huesped();

        System.out.println("INGRESE DATOS PERSONALES");
        System.out.println("Apellido y Nombres:");
        huesped.setNombre(Teclado.nextLine());
        System.out.println("DNI:");
        huesped.setDni(Teclado.nextInt());
        Teclado.nextLine(); //chupa el enter
        System.out.println("Domicilio:");
        huesped.setDomicilio(Teclado.nextLine());
        System.out.println("Domicilio alternativo(OPCIONAL):");
        String domAlternativo = Teclado.nextLine();
        if (domAlternativo != null)
            huesped.setDomicilioAlternativo(domAlternativo);      

        // Si creamos un Huesped le creamos una reserva
        Reserva reserva = new Reserva();

        reserva.setFechaReserva(new Date()); //Fecha actual

        System.out.println("Ingrese la fecha de ingreso(DD/MM/AA)");
        String fechaString = Teclado.nextLine();
        Date fechaDate = validarFecha(fechaString);
        reserva.setFechaIngreso(fechaDate);

        System.out.println("Ingrese la fecha de salida(DD/MM/AA)");
        fechaString = (Teclado.nextLine());
        fechaDate = validarFecha(fechaString);
        reserva.setFechaEgreso(fechaDate);
        
        //PAGOS
        BigDecimal importeReserva = new BigDecimal(Math.floor(Math.random()*(10000-1000+1)+1000));
        reserva.setImporteReserva(importeReserva);
        System.out.println("El valor de su reserva es: "+ importeReserva);
        System.out.println("¿Cuanto desea abonar ahora para efectivizar su reserva?");
        reserva.setImportePagado(new BigDecimal(Teclado.nextLine()));
        BigDecimal gastosExtra = new BigDecimal(2500);
        reserva.setImporteTotal(importeReserva.add(gastosExtra));
        reserva.setTipoEstadoPagoId(10);

        reserva.setHuesped(huesped); //Esta es la relacion bidireccional
        
      

        
        //Actualizo todos los objeto

        ABMHuesped.create(huesped);

        /*
         * Si concateno el OBJETO directamente, me trae todo lo que este en el metodo
         * toString() mi recomendacion es NO usarlo para imprimir cosas en pantallas, si
         * no para loguear info Lo mejor es usar:
         * System.out.println("Huesped generada con exito.  " + huesped.getHuespedId);
         */

        System.out.println("Huesped generada con exito.  " + huesped);

    }

    public void baja() {
        System.out.println("Ingrese el nombre:");
        String nombre = Teclado.nextLine();
        System.out.println("Ingrese el ID de Huesped:");
        int id = Teclado.nextInt();
        Teclado.nextLine();
        Huesped huespedEncontrado = ABMHuesped.read(id);

        if (huespedEncontrado == null) {
            System.out.println("Huesped no encontrado.");

        } else {

            try {

                ABMHuesped.delete(huespedEncontrado);
                System.out
                        .println("El registro del huesped " + huespedEncontrado.getHuespedId() + " ha sido eliminado.");
            } catch (Exception e) {
                System.out.println("Ocurrio un error al eliminar una huesped. Error: " + e.getCause());
            }

        }
    }

    public void bajaPorDNI() {
        System.out.println("Ingrese el nombre:");
        String nombre = Teclado.nextLine();
        System.out.println("Ingrese el DNI de Huesped:");
        int dni = Teclado.nextInt();
        Huesped huespedEncontrado = ABMHuesped.readByDNI(dni);

        if (huespedEncontrado == null) {
            System.out.println("Huesped no encontrado.");

        } else {
            ABMHuesped.delete(huespedEncontrado);
            System.out.println("El registro del DNI " + huespedEncontrado.getDni() + " ha sido eliminado.");
        }
    }

    public void modifica() throws Exception {
        // System.out.println("Ingrese el nombre de la huesped a modificar:");
        // String n = Teclado.nextLine();

        System.out.println("Ingrese el ID de la huesped a modificar:");
        int id = Teclado.nextInt();
        Teclado.nextLine();
        Huesped huespedEncontrado = ABMHuesped.read(id);

        if (huespedEncontrado != null) {

            // RECOMENDACION NO USAR toString(), esto solo es a nivel educativo.
            System.out.println(huespedEncontrado.toString() + " seleccionado para modificacion.");

            System.out.println(
                    "Elija qué dato de la huesped desea modificar: \n1: nombre, \n2: DNI, \n3: domicilio, \n4: domicilio alternativo");
            int selecper = Teclado.nextInt();

            switch (selecper) {
                case 1:
                    System.out.println("Ingrese el nuevo nombre:");
                    Teclado.nextLine();
                    huespedEncontrado.setNombre(Teclado.nextLine());

                    break;
                case 2:
                    System.out.println("Ingrese el nuevo DNI:");
                    Teclado.nextLine();
                    huespedEncontrado.setDni(Teclado.nextInt());
                    Teclado.nextLine();

                    break;
                case 3:
                    System.out.println("Ingrese el nuevo domicilio:");
                    Teclado.nextLine();
                    huespedEncontrado.setDomicilio(Teclado.nextLine());

                    break;
                case 4:
                    System.out.println("Ingrese el nuevo domicilio alternativo:");
                    Teclado.nextLine();
                    huespedEncontrado.setDomicilioAlternativo(Teclado.nextLine());

                    break;

                default:
                    break;
            }

            // Teclado.nextLine();

            ABMHuesped.update(huespedEncontrado);

            System.out.println("El registro de " + huespedEncontrado.getNombre() + " ha sido modificado.");

        } else {
            System.out.println("Huesped no encontrado.");
        }

    }

    public void listar() {

        List<Huesped> todos = ABMHuesped.buscarTodos();
        for (Huesped c : todos) {
            mostrarHuesped(c);
        }
    }

    public void listarPorNombre() {

        System.out.println("Ingrese el nombre:");
        String nombre = Teclado.nextLine();

        List<Huesped> huespedes = ABMHuesped.buscarPor(nombre);
        for (Huesped huesped : huespedes) {
            mostrarHuesped(huesped);
        }
    }

    public void mostrarHuesped(Huesped huesped) {

        System.out.print("Id: " + huesped.getHuespedId() + " Nombre: " + huesped.getNombre()
        + " DNI: " + huesped.getDni()
        + " Domicilio: " + huesped.getDomicilio());

        if (huesped.getDomicilioAlternativo() != null)
            System.out.println(" Alternativo: " + huesped.getDomicilioAlternativo());
        else
            System.out.println();
    }

    public static void printOpciones() {
        System.out.println("=======================================");
        System.out.println("");
        System.out.println("1. CREAR RESERVA");
        System.out.println("2. MODIFICAR RESERVA");
        System.out.println("3. CONSULTAR RESERVA");
        System.out.println("0. Para terminar.");
        System.out.println("");
        System.out.println("=======================================");
    }

    public Date validarFecha(String f){
    boolean fechaIncorrecta = true;
    Date fecha = null;
    while (fechaIncorrecta) {       
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
            fecha = simpleDateFormat.parse(f);
            fechaIncorrecta = false;

        } catch(Exception ex){
            System.out.println("Error. Ingrese una fecha valida en formato DD-MM-AA ");
            f= Teclado.nextLine();
            
        }
        
    }
    return fecha;

    }
}