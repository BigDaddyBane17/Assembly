package com.example.computershop.presentation.client_screens

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.computershop.R
import com.example.computershop.data.database.DatabaseHelper
import com.example.computershop.domain.Component
import java.text.NumberFormat



fun getLocalComponents(): List<Component> {
    return listOf(

        Component(1, "Процессор", "Intel Core i3-12100F", 8990.0, "https://avatars.mds.yandex.net/i?id=6ab66167c175135a4a5470882ce6f60c_l-8196260-images-thumbs&n=13", "LGA1700"),
        Component(2, "Процессор", "AMD Ryzen 3 4100", 7490.0, "https://m.media-amazon.com/images/I/71AaBKUVeoL._AC_UF1000,1000_QL80_.jpg", "AM4"),
        Component(3, "Процессор", "Intel Core i5-12400", 15790.0, "https://avatars.dzeninfra.ru/get-zen_doc/271828/pub_65959f16c8a9ac37558ef672_6595a337865a591d8bba437e/scale_1200", "LGA1700"),
        Component(4, "Процессор", "AMD Ryzen 5 5600X", 18990.0, "https://static.onlinetrade.ru/img/fullreviews/99040/8_small.jpg", "AM4"),
        Component(5, "Процессор", "Intel Core i7-13700K", 42990.0, "https://i.pinimg.com/originals/91/c7/68/91c768ced706e44de8a7e02ba7a17c4e.jpg", "LGA1700"),
        Component(6, "Процессор", "AMD Ryzen 7 5800", 45990.0, "https://avatars.mds.yandex.net/i?id=6ba16ed6ae7bc9a7d84774526029683e_l-5666076-images-thumbs&n=13", "AM4"),


        Component(7, "Материнская плата", "ASUS PRIME B560M-A", 9590.0, "https://avatars.mds.yandex.net/i?id=4eb3bc9818c14ebf962be84e2b29c456cf98773f-4432629-images-thumbs&n=13", "LGA1700", "DDR4"),
        Component(8, "Материнская плата", "MSI MAG B550 TOMAHAWK", 11990.0, "https://i2.wp.com/itc.ua/wp-content/uploads/2020/07/msi_mag_b550_tomahawk_10-scaled.jpg?quality%5Cu003d100%5Cu0026strip%5Cu003dall%5Cu0026ssl%5Cu003d1", "AM4", "DDR4"),
        Component(9, "Материнская плата", "Gigabyte Z690 AORUS ELITE", 24990.0, "https://avatars.mds.yandex.net/i?id=bcc6948f9c1bc206137543e28b6862eb2c2eb1c8-10871820-images-thumbs&n=13", "LGA1700", "DDR4"),
        Component(10, "Материнская плата", "ASRock B450M PRO4", 8490.0, "https://avatars.mds.yandex.net/i?id=0f17f7357e5e221781d0f8f2c047d33b7a176bb1-2440567-images-thumbs&n=13", "AM4", "DDR4"),
        Component(11, "Материнская плата", "ASUS ROG STRIX X570-E", 28990.0, "https://avatars.mds.yandex.net/i?id=58085b3a9238bce346f7df6a50f7b58e_l-5284066-images-thumbs&n=13", "AM4", "DDR4"),
        Component(12, "Материнская плата", "MSI Z790 GAMING EDGE WIFI", 31990.0, "https://avatars.mds.yandex.net/i?id=cb1487ef0be8617fd306720d089803cc_l-3979407-images-thumbs&n=13", "LGA1700", "DDR5"),


        Component(13, "ОЗУ", "Kingston FURY Beast KF552C40BBK2 16 GB", 6490.0, "https://avatars.mds.yandex.net/get-mpic/6336603/2a00000191a46277d3ca7121f389d2ca339b/900x1200", memoryType = "DDR5"),
        Component(14, "ОЗУ", "Kingston Fury Beast 16GB", 5990.0, "https://andpro.ru/upload/iblock/eed/tyyki8862g1ergwq85bv4ql6vfydthtz/cc163f3d_6cbc_11ed_8134_001e67d1aaeb_7dfa1148_008a_11ef_8148_001e67d1aaea.jpg", memoryType = "DDR4"),
        Component(15, "ОЗУ", "G.Skill Trident Z RGB 32GB", 12990.0, "https://avatars.mds.yandex.net/i?id=0d419fc3f7ad51db8a16d6ecb2b85783edf54ee1-12734301-images-thumbs&n=13", memoryType = "DDR4"),
        Component(16, "ОЗУ", "HyperX Predator 32GB", 12490.0, "https://avatars.mds.yandex.net/i?id=d00d7581f93a690dc912369ecd314d9c_l-6498965-images-thumbs&n=13", memoryType = "DDR4"),
        Component(17, "ОЗУ", "Corsair Dominator Platinum 64GB", 29990.0, "https://cdn1.ozone.ru/s3/multimedia-m/6392400454.jpg","DDR4" ),
        Component(18, "ОЗУ", "ADATA XPG GAMMIX D30 8GB", 3490.0, "https://avatars.mds.yandex.net/i?id=2d9a23292d3e214888475597c8822a9060611f6d-7047516-images-thumbs&n=13", memoryType = "DDR4"),


        Component(19, "Блок питания", "Corsair CV450 450W", 3290.0, "https://avatars.mds.yandex.net/i?id=a0336562f0c4630bdcc3c68c5e7e36d5_l-5875509-images-thumbs&n=13"),
        Component(20, "Блок питания", "Cooler Master MWE 550W", 4990.0, "https://static.onlinetrade.ru/img/users_images/301221/b/blok_pitaniya_cooler_master_mwe_white_550w_v2_mpe_5501_acabw_eu_1591141277_1.jpg"),
        Component(21, "Блок питания", "Thermaltake Toughpower GF1 750W", 8490.0, "https://regard-store.ru/upload/iblock/7fa/bwpfrcn5p62zeobcni04g3obc3a2z0q1.jpg"),
        Component(22, "Блок питания", "Be Quiet! Straight Power 11 850W", 13490.0, "https://al-fullsize.object.pscloud.io/files/alfa/messages/1/2/6/0/2/12602086-be-quiet-straight-power-11-850w.jpg"),


        Component(23, "Видеокарта", "NVIDIA GeForce RTX 3060", 34990.0, "https://avatars.mds.yandex.net/i?id=939cd6a7f1759a69914414136966169a7a0bb084-4316934-images-thumbs&n=13"),
        Component(24, "Видеокарта", "AMD Radeon RX 6600", 26990.0, "https://andpro.ru/upload/iblock/bb4/garn2duk1q5wh3f3xe4hil911t1cmtxx/62b26899_e05f_11ee_8148_001e67d1aaea_5de60844_0b9c_11ef_8148_001e67d1aaea.jpg"),
        Component(25, "Видеокарта", "NVIDIA GeForce RTX 3080", 79990.0, "https://cdna.artstation.com/p/assets/images/images/030/023/616/large/muhammx-alnazir-rtx3080-r1.jpg?1599371316"),
        Component(26, "Видеокарта", "AMD Radeon RX 6700 XT", 48990.0, "https://www.notebookcheck.net/fileadmin/Notebooks/AMD/RX_6700_XT/AMD_Radeon_RX_6700_XT_Graphics_Card_1.jpg"),


        Component(27, "Системный блок", "NZXT H510", 4990.0, "https://newmart.ru/upload/iblock/13e/13e352796386e4c0ff6879bcf84bc3d4.jpg"),
        Component(28, "Системный блок", "Cooler Master MasterBox Q300L", 4290.0, "https://avatars.mds.yandex.net/i?id=2b59d5ef26e546ed433429d383ee32fa_l-8000733-images-thumbs&n=13"),
        Component(29, "Системный блок", "Corsair 4000D Airflow", 7490.0, "https://assets.corsair.com/image/upload/c_pad,q_auto,h_1024,w_1024,f_auto/products/Cases/base-4000d-airflow-config/Gallery/4000D_AF_WHITE_18.webp"),
        Component(30, "Системный блок", "Fractal Design Meshify C", 8990.0, "https://avatars.mds.yandex.net/i?id=377640e421ac36c5135b0712756a068662aaa83d-5855963-images-thumbs&n=13"),


        Component(31, "HDD", "WD Blue 1TB", 2990.0, "https://avatars.mds.yandex.net/i?id=d078fd11cf505f52de6b1883e8e1666862ff983e-10805353-images-thumbs&n=13"),
        Component(32, "SSD", "Samsung 970 EVO Plus 1TB", 8490.0, "https://static.onlinetrade.ru/img/users_images/266927/b/ssd_disk_samsung_970_evo_m.2_1tb_pcie_gen_3.0_x4_v_nand_3bit_mlc_mz_v7e1t0bw_1572194528_1.jpg"),

        Component(33, "HDD","Seagate Barracuda 2TB", 5400.0,  "https://avatars.mds.yandex.net/i?id=7efa98fb7c98d8ef9bf820a5ec590a24_l-5878560-images-thumbs&n=13"),
        Component(34, "HDD", "Western Digital Blue 1TB", 2800.0,  "https://pc-1.ru/pic/medium/1062902.jpg"),
        Component(35, "SSD","Kingston A2000 500GB", 4650.0, "https://avatars.mds.yandex.net/i?id=ed72e3d7c376bfd85ff2eee126a96718_l-5233757-images-thumbs&n=13")
    )
}




fun isCompatible(type: String, component: Component, selectedComponents: Map<String, Component>): Boolean {
    return when (type) {
        "Процессор" -> true
        "Материнская плата" -> {
            val processor = selectedComponents["Процессор"]
            processor == null || processor.socket == component.socket
        }
        "ОЗУ" -> {
            val motherboard = selectedComponents["Материнская плата"]
            motherboard?.memoryType == component.memoryType
        }
        else -> true
    }
}

fun clearIncompatibleComponents(
    type: String,
    updatedComponent: Component,
    selectedComponents: MutableMap<String, Component>
) {
    selectedComponents.keys.toList().forEach { key ->
        if (!isCompatible(key, selectedComponents[key]!!, selectedComponents.apply { put(type, updatedComponent) })) {
            selectedComponents.remove(key)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrderScreen(
    idClient: Int,
    onOrderAdded: () -> Unit,
    databaseHelper: DatabaseHelper
) {
    var description by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var selectedComponents by remember { mutableStateOf<Map<String, Component>>(emptyMap()) }
    var error by remember { mutableStateOf("") }
    var totalPrice by remember { mutableStateOf(0.0) }

    val componentTypes = listOf(
        "Процессор", "Материнская плата", "ОЗУ", "Блок питания",
        "Видеокарта", "Системный блок", "HDD", "SSD"
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Добавление заказа",
                        fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = name,
                singleLine = true,
                onValueChange = { newText -> name = newText },
                label = { Text("Название заказа", fontFamily = FontFamily(Font(R.font.montserrat_light))) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Описание заказа", fontFamily = FontFamily(Font(R.font.montserrat_light))) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                maxLines = 10,
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            componentTypes.forEach { type ->
                Text(
                    text = "$type:",
                    fontFamily = FontFamily(Font(R.font.montserrat_light)),
                    modifier = Modifier.fillMaxWidth()
                )
                ComponentDropdown(
                    type = type,
                    selectedComponent = selectedComponents[type],
                    selectedComponents = selectedComponents,
                    onComponentSelected = { component ->
                        selectedComponents = selectedComponents.toMutableMap().apply {
                            put(type, component)
                            if (type == "Процессор" || type == "Материнская плата") {
                                clearIncompatibleComponents(type, component, this)
                            }
                        }
                        totalPrice = selectedComponents.values.sumOf { it.price }
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Общая сумма заказа: ${NumberFormat.getCurrencyInstance().format(totalPrice)}",
                fontFamily = FontFamily(Font(R.font.montserrat_light))
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (error.isNotEmpty()) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    fontFamily = FontFamily(Font(R.font.montserrat_light))
                )
            }

            var isProcessing by remember { mutableStateOf(false) }

            Button(
                onClick = {
                    if (!isProcessing) {
                        isProcessing = true
                        if (name.isBlank() || description.isBlank() || selectedComponents.size < componentTypes.size) {
                            error = "Пожалуйста, заполните все поля и выберите все комплектующие."
                            isProcessing = false
                        } else {
                            val componentsList = selectedComponents.values.toList()
                            databaseHelper.insertOrder(
                                description = description.trim(),
                                name = name.trim(),
                                price = totalPrice,
                                imageUri = null,
                                idEmployee = null,
                                idClient = idClient,
                                idStatus = 1,
                                components = componentsList
                            )
                            onOrderAdded()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !isProcessing,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Добавить заказ",
                    fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                )
            }


        }
    }
}

@Composable
fun ComponentDropdown(
    type: String,
    selectedComponent: Component?,
    selectedComponents: Map<String, Component>,
    onComponentSelected: (Component) -> Unit
) {
    val allComponents = remember { getLocalComponents() }
    val compatibleComponents = remember(selectedComponents) {
        allComponents.filter { component ->
            component.type == type && isCompatible(type, component, selectedComponents)
        }
    }
    var expanded by remember { mutableStateOf(false) }

    Box {
        Text(
            text = selectedComponent?.name ?: "Выбрать $type",
            fontFamily = FontFamily(Font(R.font.montserrat_light)),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .border(1.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium)
                .padding(8.dp)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            compatibleComponents.forEach { component ->
                DropdownMenuItem(
                    onClick = {
                        onComponentSelected(component)
                        expanded = false
                    },
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = component.name,
                                fontFamily = FontFamily(Font(R.font.montserrat_light)),
                                modifier = Modifier.weight(1f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = NumberFormat.getCurrencyInstance().format(component.price),
                                fontFamily = FontFamily(Font(R.font.montserrat_light)),
                                modifier = Modifier.wrapContentWidth(Alignment.End)
                            )
                        }
                    }
                )
            }
        }
    }
}

