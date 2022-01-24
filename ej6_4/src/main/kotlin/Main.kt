//Package un5.eje5_4

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import java.io.FileNotFoundException
import javax.xml.parsers.DocumentBuilderFactory
import java.util.logging.Level
import java.util.logging.LogManager

val l = LogManager.getLogManager().getLogger("").apply { level = Level.ALL }


/**
 * Clase para el catálogo de libros xml
 */
data class CatalogoLibrosXML(val cargador: String){
    val doc : Document? = try{readXml(cargador)}catch(e:Exception){
        l.warning("Vacío o erróneo")
        null}
    init{

    }

    /**
     * Función para crear la representación del xml
     */
    fun readXml(pathName: String): Document {
        val xmlFile = File(pathName)
        return  DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile)}

    /**
     *  Función para ver si existe libro
     */
    fun existeLibro(idLibro: String): Boolean{
        var bool = false
        var lista = obtenerListaNodosPorNombre("book")
        lista.forEach{
            it as Element
            var map = obtenerAtributosEnMapKV(it)
            if(idLibro in map){ bool= true}
        }
        when(bool){
            true -> return true
            false -> return false
        }

    }
    /**
     * Función para obtener los nodos
     */
    fun obtenerListaNodosPorNombre( tagName: String): MutableList<Node>
    {
        //Le he puesto lo de "!!" porque me dijiste en clase que se lo pusiera
        val bookList: NodeList = doc?.getElementsByTagName(tagName) !!
        val lista = mutableListOf<Node>()
        for (i in 0..bookList.length - 1)
            lista.add(bookList.item(i))
        return lista
    }
    /**
     * Función para obtener los atributos
     */
    fun obtenerAtributosEnMapKV(e: Element ):MutableMap<String, String>
    {
        var lista = obtenerListaNodosPorNombre("book")
        val mMap = mutableMapOf<String, String>()
        lista.forEach{
            for(j in 0..e.attributes.length - 1)
                mMap.put(e.attributes.item(j).nodeValue,e.attributes.item(j).nodeName)}
        return mMap
    }
    /**
     *  Función para obtener los datos de un libro por su id
     */
    fun infoLibro(idLibro:String): Map<String, MutableMap<String, String>> {
        val lista = obtenerListaNodosPorNombre("book")
        var mMap: MutableMap<String,MutableMap<String,String>> = mutableMapOf()
        lista.forEach{
            it.getNodeType() === Node.ELEMENT_NODE
            val elem = it as Element
            val map: MutableMap<String,String> = mutableMapOf()
            map.set ("Autor","${it.getElementsByTagName("author").item(0).textContent}")
            map.set ("Título","${it.getElementsByTagName("title").item(0).textContent}")
            map.set ("Género","${it.getElementsByTagName("genre").item(0).textContent}")
            map.set ("Precio","${it.getElementsByTagName("price").item(0).textContent}")
            map.set ("Fecha","${it.getElementsByTagName("publish_date").item(0).textContent}")
            map.set ("Descripción","${it.getElementsByTagName("description").item(0).textContent}")
            mMap.set("${obtenerAtributosEnMapKV(elem).keys}",map)
        }
        if("$idLibro" in mMap.keys) {
            var map2 = mMap.getValue("$idLibro")
            val map3: Map<String, MutableMap<String, String>> = mapOf("$idLibro" to map2)
            return map3
        }
        else return emptyMap()
    }


}
fun main() {
    var xmlDoc = CatalogoLibrosXML("C:\\Users\\Mayka\\Documents\\GitHub\\eje5-4\\eje5-4\\catalogo.xml")
    var lista = xmlDoc.obtenerListaNodosPorNombre("book")
    var mMap: MutableMap<String,String> = mutableMapOf()
    println(xmlDoc.existeLibro("bk101"))
    println(xmlDoc.existeLibro("bg202"))
    println(xmlDoc.infoLibro("[bk102]"))
    println(xmlDoc.infoLibro("[bk206]"))
}
