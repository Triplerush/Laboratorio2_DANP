package com.example.laboratorio02

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EdificacionesApp()
        }
    }
}

data class Edificacion(
    val title: String,
    val category: String,
    val description: String,
    val imageRes: Int
)

enum class FilterType { TITLE, CATEGORY, DESCRIPTION }

@Composable
fun EdificacionesApp() {
    // estado de búsqueda y filtro
    var searchText by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf(FilterType.TITLE) }

    // lista original
    val all = remember {
        listOf(
            Edificacion("Catedral", "Iglesia",
                "Santuario principal de la ciudad, ocupando el lado norte de la Plaza de Armas.",
                R.drawable.house),
            Edificacion("Mansión del Fundador", "Casona",
                "La Mansión del Fundador es una histórica casona colonial de Arequipa, conocida por su arquitectura de sillar y su rica herencia cultural y artística.",
                R.drawable.house),
            Edificacion("Monasterio de Santa Catalina", "Monasterio",
                "Una pequeña ciudadela que ocupa un área de 20 mil metros cuadrados.",
                R.drawable.house),
            Edificacion("Molino de Sabandía", "Molino",
                "Una construcción colonial donde se molían trigo y maíz.",
                R.drawable.house),
            Edificacion("Casa del Moral", "Casona",
                "Una de las casas coloniales más antiguas de Arequipa, famosa por su arquitectura y mobiliario de época.",
                R.drawable.house),
            Edificacion("Iglesia de La Compañía", "Iglesia",
                "Una iglesia barroca construida por la Compañía de Jesús en el siglo XVII, con una impresionante fachada tallada.",
                R.drawable.house),
            Edificacion("Claustros de la Compañía", "Claustro",
                "Un conjunto arquitectónico colonial que forma parte del complejo de la iglesia de La Compañía.",
                R.drawable.house),
            Edificacion("Basílica Menor de La Merced", "Iglesia",
                "Iglesia colonial conocida por su arquitectura y su impactante altar mayor.",
                R.drawable.house),
            Edificacion("Casa Tristán del Pozo", "Casona",
                "Una casona colonial bien conservada, actualmente sede de una entidad bancaria.",
                R.drawable.house),
            Edificacion("Iglesia y Convento de Santo Domingo", "Convento",
                "Un hermoso templo colonial con un interior decorado en estilo barroco.",
                R.drawable.house),
            Edificacion("Museo de Arte Virreinal de Santa Teresa", "Museo",
                "Convento y museo que alberga una rica colección de arte virreinal.",
                R.drawable.house),
            Edificacion("Museo Santuarios Andinos", "Museo",
                "Museo famoso por albergar a la Momia Juanita, una de las momias mejor conservadas del mundo.",
                R.drawable.house),
            Edificacion("Iglesia San Agustín", "Iglesia",
                "Iglesia barroca con una fachada de estilo rococó, ubicada en el centro histórico de Arequipa.",
                R.drawable.house),
            Edificacion("Iglesia Santa Marta", "Iglesia",
                "Templo del siglo XVI, caracterizado por su estilo sencillo y su fachada de sillar.",
                R.drawable.house),
            Edificacion("Casona del Solar", "Casona",
                "Construcción del siglo XVIII, un símbolo del esplendor colonial en Arequipa.",
                R.drawable.house),
            Edificacion("Teatro Fénix", "Teatro",
                "Antiguo teatro de Arequipa, conocido por ser uno de los primeros teatros del Perú.",
                R.drawable.house)
        )
    }

    // filtrado según texto y criterio
    val filtered = all.filter { ed ->
        val txt = searchText.lowercase()
        when(filter) {
            FilterType.TITLE       -> ed.title.lowercase().contains(txt)
            FilterType.CATEGORY    -> ed.category.lowercase().contains(txt)
            FilterType.DESCRIPTION -> ed.description.lowercase().contains(txt)
        }
    }

    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            SearchBar(searchText) { searchText = it }
            FilterOptions(filter) { filter = it }
            Text(
                text = "Lista de edificaciones",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
            EdificacionesList(items = filtered)
        }
    }
}

@Composable
fun SearchBar(text: String, onTextChange: (String)->Unit) {
    OutlinedTextField(
        value       = text,
        onValueChange = onTextChange,
        placeholder = { Text("Buscar edificaciones") },
        modifier    = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Composable
fun FilterOptions(selected: FilterType, onSelect: (FilterType)->Unit) {
    Row(modifier = Modifier.padding(horizontal = 16.dp)) {
        RadioButton(
            selected = selected == FilterType.TITLE,
            onClick  = { onSelect(FilterType.TITLE) }
        )
        Text("Título", modifier = Modifier
            .padding(start = 4.dp, end = 16.dp)
            .clickable { onSelect(FilterType.TITLE) }
        )
        RadioButton(
            selected = selected == FilterType.CATEGORY,
            onClick  = { onSelect(FilterType.CATEGORY) }
        )
        Text("Categoría", modifier = Modifier
            .padding(start = 4.dp, end = 16.dp)
            .clickable { onSelect(FilterType.CATEGORY) }
        )
        RadioButton(
            selected = selected == FilterType.DESCRIPTION,
            onClick  = { onSelect(FilterType.DESCRIPTION) }
        )
        Text("Descripción", modifier = Modifier
            .padding(start = 4.dp)
            .clickable { onSelect(FilterType.DESCRIPTION) }
        )
    }
}

@Composable
fun EdificacionesList(items: List<Edificacion>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items) { ed ->
            EdificacionItem(ed)
            Divider()
        }
    }
}

@Composable
fun EdificacionItem(ed: Edificacion) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Image(
            painter           = painterResource(id = ed.imageRes),
            contentDescription = ed.title,
            modifier          = Modifier
                .size(64.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text     = ed.title,
                style    = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text  = ed.category,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text  = ed.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEdificacionesApp() {
    EdificacionesApp()
}
