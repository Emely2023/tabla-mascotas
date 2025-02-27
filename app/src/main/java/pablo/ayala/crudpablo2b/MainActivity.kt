package pablo.ayala.crudpablo2b

import Modelo.ClaseConnection
import Modelo.dataClassMascotas
import RecyclerViewHelper.Adaptador
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //1- Mandar a llamar a todos los elementos
        val txtNombre = findViewById<EditText>(R.id.txtNombre)
        val txtPeso = findViewById<EditText>(R.id.txtPeso)
        val txtEdad = findViewById<EditText>(R.id.txtEdad)
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)
        val rcvMascotas = findViewById<RecyclerView>(R.id.rcvMascotas)

        //1- Asignar un layout al RecyclerView para mostrar datos
        rcvMascotas.layoutManager = LinearLayoutManager(this)

        //Función para mostrar datos
        fun ObtenerDatos(): List<dataClassMascotas> {
            //Paso 1: Crear un objeto de la clase Connection
            val objConexion = ClaseConnection().cadenaConexion()

            //Paso 2: Crear un Statement
            val statement = objConexion?.createStatement()
            val resulSet = statement?.executeQuery("select * from tbMascotas")!!
            val mascotas = mutableListOf<dataClassMascotas>()

            //Recorro todos los registros de la base de datos
            while (resulSet.next()) {
                val nombre = resulSet.getString("nombremascota")
                val mascota = dataClassMascotas(nombre)
                mascotas.add(mascota)
            }
            return mascotas
        }

        //Asignar adaptador al RecyclerView
        CoroutineScope(Dispatchers.IO).launch {
            val BDmascotas = ObtenerDatos()
            withContext(Dispatchers.Main) {
                val adapter = Adaptador(BDmascotas)
                rcvMascotas.adapter = adapter
            }
        }

        //2- Programar para el botón agregar
        btnAgregar.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                //1 Creo un objeto de la clase conexion
                val objConexion = ClaseConnection().cadenaConexion()

                //2- Creo una variable que contenga un PrepareStatement
                val addMascota = objConexion?.prepareStatement("insert into tbMascotas values (?,?,?)")!!
                addMascota.setString(1, txtNombre.text.toString())
                addMascota.setInt(2, txtPeso.text.toString().toInt())
                addMascota.setInt(3, txtEdad.text.toString().toInt())
                addMascota.executeUpdate()

            }
        }

    }
}