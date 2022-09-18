package com.example.michiexamen

import android.R
import android.graphics.Color
import android.graphics.Color.*
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.michiexamen.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    enum class Turno{ //Utilizamos un enum, ya que solo son dos tipos de turno.
            Cruz,
            Circulo
    }
    //Listas en donde depositaremos las letras y colores para luego escoger uno aleatorio.
    var ArrayLetras= mutableListOf<String>()
    var ArrayColores= mutableListOf<Int>()
    //Inicializamos en el primer turno

    //Es necesario saber en que turno nos encontramos y con que turno empezamos
    //SIempre se empieza con cruz.
    private var PrimerTurno = Turno.Cruz
    private var turnoactivo= Turno.Cruz

    //Estas dos variables sirven para saber que letra corresponde, y si se ya se jugo con X o O
    private var valorCirculo= ""
    private var valorCruz= " "
    private var PrimeraPartida = true
    private var sepuedepintar=true //Bool que funciona para que no se pueda pintar una vez alguien gane
    //Array para guardar los botones
    private var ArrayBotones = mutableListOf<Button>()
    //Binding para conectar con el xml y poder trabajar con los id's.
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        iniciarTablero()
        iniciarListLetras()
        iniciarListColores()
        escogerLetras()
    }
    private fun iniciarListLetras() { //Inicializamos Lista Letras
        ArrayLetras.add(0,"A");
        ArrayLetras.add(1,"B");
        ArrayLetras.add(2,"C");
        ArrayLetras.add(3,"D");
    }

    //Establecemos la lista de colores
    private fun iniciarListColores() {
        ArrayColores.add(BLACK)
        ArrayColores.add(YELLOW)
        ArrayColores.add(GREEN)
        ArrayColores.add(Color.CYAN)
        ArrayColores.add(Color.BLUE)
    }

    //Escogemos la letra aleatoria, luego de que no sea la primera partida
    private fun escogerLetras() : Unit {
        if(PrimeraPartida){ //Si es la primera partida se juega con X o O
            valorCruz = "X"
            valorCirculo ="0"
        }else{ //Si no, escogemos letras
            var letraRandom = escogerAleatorio()
            var letraRandom2 = escogerAleatorio()
            valorCruz = ArrayLetras[letraRandom]
            valorCirculo= ArrayLetras[letraRandom2]
            if(valorCirculo == valorCruz){
                escogerLetras() //SI son iguales vuelve a escoger
            }
        }
    }

    private fun escogerAleatorio() : Int {
        var letraRandom = (0 until ArrayLetras.size).random()
        return letraRandom
    } //Funcion para escoger Aleatorio de letras



    //Añadimos los botones al array.
    fun iniciarTablero(){
        ArrayBotones.add(binding.a1)
        ArrayBotones.add(binding.a2)
        ArrayBotones.add(binding.a3)
        ArrayBotones.add(binding.b1)
        ArrayBotones.add(binding.b2)
        ArrayBotones.add(binding.b3)
        ArrayBotones.add(binding.c1)
        ArrayBotones.add(binding.c2)
        ArrayBotones.add(binding.c3)

    }
    fun TableroClick(view: View) {
        if(view !is Button) { //Si el view, no es un boton entonces no hagas nada
            return
        }else { //Sino, significa que es un boton por lo que podemos cambiar su texto.
            setValor(view)
            if(!sepuedepintar){//Si no se puede pintar entonces termino la partida
                restTablero(view) //Manda a reiniciar.
            }
        }
        if(esVictoria(valorCruz)){ //Se comprueba si gano el primer jugador
            binding.turno.text="Victoria de ${valorCruz}"
            sepuedepintar = false //Como termino la partida entonces no se puede pintar
        }
        if(esVictoria(valorCirculo)){ //Se comprueba si gano el segundo jugador
            binding.turno.text="Victoria de ${valorCirculo}"
            sepuedepintar = false //Como termino la partida entonces no se puede pintar

        }
        if(estaLleno()){//Si el tablero esta lleno
            if(sepuedepintar){ //Si aun nadie ha ganado entonces
                binding.turno.text ="El resultado es un empate"
                sepuedepintar = false //Como termino la partida entonces no se puede pintar
            }
        }
    }

    //Comprobamos las distintas victorias del juego.
    private fun esVictoria(valor: String): Boolean
    {
        //Victoria en horizontal
        if(hicieronMatch(binding.a1,valor) && hicieronMatch(binding.a2,valor) && hicieronMatch(binding.a3,valor) ){
            return true
        }
        if(hicieronMatch(binding.b1,valor) && hicieronMatch(binding.b2,valor) && hicieronMatch(binding.b3,valor) ){
            return true
        }
        if(hicieronMatch(binding.c1,valor) && hicieronMatch(binding.c2,valor) && hicieronMatch(binding.c3,valor) ){
            return true
        }
        //Victoria en vertical
        if(hicieronMatch(binding.a1,valor) && hicieronMatch(binding.b1,valor) && hicieronMatch(binding.c1,valor) ){
            return true
        }
        if(hicieronMatch(binding.a2,valor) && hicieronMatch(binding.b2,valor) && hicieronMatch(binding.c2,valor) ){
            return true
        }
        if(hicieronMatch(binding.a3,valor) && hicieronMatch(binding.b3,valor) && hicieronMatch(binding.c3,valor) ){
            return true
        }
        //Victoria en Diagonal
        if(hicieronMatch(binding.a1,valor) && hicieronMatch(binding.b2,valor) && hicieronMatch(binding.c3,valor) ){
            return true
        }
        if(hicieronMatch(binding.a3,valor) && hicieronMatch(binding.b2,valor) && hicieronMatch(binding.c1,valor) ){
            return true
        }
        return false
    }

    //Verifica si el boton asignado tiene el valor que corresponde
    private fun hicieronMatch(boton : Button, valor: String): Boolean {
        if(boton.text==valor){return true}
        return false
    }

    //Resetea el tablero
    fun restTablero(view:View) {
        var colorrandom = (0 until ArrayColores.size).random() //Elige un color aleatorio
        for(button in ArrayBotones){ //Para cada boton en el array
            button.text = ""//Lo Limpia
            button.setBackgroundColor(ArrayColores[colorrandom]) //Le coloca el color aleatorio al background
        }
        if(PrimerTurno == Turno.Circulo){ PrimerTurno = Turno.Cruz} //Se invierte los turnos
        else if(PrimerTurno == Turno.Cruz){ PrimerTurno = Turno.Circulo} //Se invierten los turnos
        PrimeraPartida =false //Ya paso la primera partida entonces letras deben cambiar
        sepuedepintar = true
        escogerLetras() //Se escoge una letra aleatoria
        setTexto() //Se cambia el texto
    }


    private fun estaLleno(): Boolean { //Devuelve si esta lleno el tablero
        for(button in ArrayBotones){ //Para cada boton en el tablero
            if(button.text == ""){ //si hay algo limpio esto es falso
                return false
            }
        }
        return true
    }

    private fun setValor(boton: Button) {
        if(sepuedepintar){ //Comprobamos si se puede pintar
            if(boton.text != ""){//Ya esta pintado, entonces no pasa nada. No se debe cambiar
                return
            }else{
                if(turnoactivo == Turno.Circulo){ //Se pone el valor del circulo en el turno del circulo
                    boton.text= valorCirculo
                    turnoactivo=Turno.Cruz
                }else if (turnoactivo == Turno.Cruz){//Se pone el valor de la cruz en el turno de la cruz
                    boton.text= valorCruz
                    turnoactivo=Turno.Circulo
                }
                setTexto() //Seteamos texto
            }
        }else{
            return
        }
    }

    private fun setTexto() : Unit { //Se setea el turno que corresponde en el textview
        var texto = ""
        if(turnoactivo == Turno.Circulo) {  texto= "Es el turno de: $valorCirculo"}
        else if(turnoactivo == Turno.Cruz){ texto = "Es el turno de $valorCruz"}
        binding.turno.text= texto;
    }


}