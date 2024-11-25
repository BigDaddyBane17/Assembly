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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.computershop.data.database.DatabaseHelper
import com.example.computershop.domain.Component
import java.text.NumberFormat



fun getLocalComponents(): List<Component> {
    return listOf(
        // Processors
        Component(1, "Processor", "Intel Core i3-12100F", 8990.0, "https://avatars.mds.yandex.net/i?id=6ab66167c175135a4a5470882ce6f60c_l-8196260-images-thumbs&n=13"),
        Component(2, "Processor", "AMD Ryzen 3 4100", 7490.0, "https://m.media-amazon.com/images/I/71AaBKUVeoL._AC_UF1000,1000_QL80_.jpg"),
        Component(3, "Processor", "Intel Core i5-12400", 15790.0, "https://avatars.dzeninfra.ru/get-zen_doc/271828/pub_65959f16c8a9ac37558ef672_6595a337865a591d8bba437e/scale_1200"),
        Component(4, "Processor", "AMD Ryzen 5 5600X", 18990.0, "https://static.onlinetrade.ru/img/fullreviews/99040/8_small.jpg"),
        Component(5, "Processor", "Intel Core i7-13700K", 42990.0, "https://i.pinimg.com/originals/91/c7/68/91c768ced706e44de8a7e02ba7a17c4e.jpg"),
        Component(6, "Processor", "AMD Ryzen 7 5800", 45990.0, "https://avatars.mds.yandex.net/i?id=6ba16ed6ae7bc9a7d84774526029683e_l-5666076-images-thumbs&n=13"),

        // Motherboards
        Component(7, "Motherboard", "ASUS PRIME B560M-A", 9590.0, "https://avatars.mds.yandex.net/i?id=4eb3bc9818c14ebf962be84e2b29c456cf98773f-4432629-images-thumbs&n=13"),
        Component(8, "Motherboard", "MSI MAG B550 TOMAHAWK", 11990.0, "https://i2.wp.com/itc.ua/wp-content/uploads/2020/07/msi_mag_b550_tomahawk_10-scaled.jpg?quality%5Cu003d100%5Cu0026strip%5Cu003dall%5Cu0026ssl%5Cu003d1"),
        Component(9, "Motherboard", "Gigabyte Z690 AORUS ELITE", 24990.0, "https://avatars.mds.yandex.net/i?id=bcc6948f9c1bc206137543e28b6862eb2c2eb1c8-10871820-images-thumbs&n=13"),
        Component(10, "Motherboard", "ASRock B450M PRO4", 8490.0, "https://avatars.mds.yandex.net/i?id=0f17f7357e5e221781d0f8f2c047d33b7a176bb1-2440567-images-thumbs&n=13"),
        Component(11, "Motherboard", "ASUS ROG STRIX X570-E", 28990.0, "https://avatars.mds.yandex.net/i?id=58085b3a9238bce346f7df6a50f7b58e_l-5284066-images-thumbs&n=13"),
        Component(12, "Motherboard", "MSI Z790 GAMING EDGE WIFI", 31990.0, "https://avatars.mds.yandex.net/i?id=cb1487ef0be8617fd306720d089803cc_l-3979407-images-thumbs&n=13"),

        // RAM
        Component(13, "RAM", "Corsair Vengeance LPX 16GB", 6490.0, "https://kotofoto.ru/product_img/2065/109864/109864_pamyat_operativnaya_ddr4_corsair_2x16gb_2400mhz_cmk32gx4m2a2400c16_1m.jpg?v=1505830341"),
        Component(14, "RAM", "Kingston Fury Beast 16GB", 5990.0, "https://andpro.ru/upload/iblock/eed/tyyki8862g1ergwq85bv4ql6vfydthtz/cc163f3d_6cbc_11ed_8134_001e67d1aaeb_7dfa1148_008a_11ef_8148_001e67d1aaea.jpg"),
        Component(15, "RAM", "G.Skill Trident Z RGB 32GB", 12990.0, "https://avatars.mds.yandex.net/i?id=0d419fc3f7ad51db8a16d6ecb2b85783edf54ee1-12734301-images-thumbs&n=13"),
        Component(16, "RAM", "HyperX Predator 32GB", 12490.0, "https://avatars.mds.yandex.net/i?id=d00d7581f93a690dc912369ecd314d9c_l-6498965-images-thumbs&n=13"),
        Component(17, "RAM", "Corsair Dominator Platinum 64GB", 29990.0, "https://cdn1.ozone.ru/s3/multimedia-m/6392400454.jpg"),
        Component(18, "RAM", "ADATA XPG GAMMIX D30 8GB", 3490.0, "https://avatars.mds.yandex.net/i?id=2d9a23292d3e214888475597c8822a9060611f6d-7047516-images-thumbs&n=13"),

        // Power Supplies
        Component(19, "Power Supply", "Corsair CV450 450W", 3290.0, "https://avatars.mds.yandex.net/i?id=a0336562f0c4630bdcc3c68c5e7e36d5_l-5875509-images-thumbs&n=13"),
        Component(20, "Power Supply", "Cooler Master MWE 550W", 4990.0, "https://static.onlinetrade.ru/img/users_images/301221/b/blok_pitaniya_cooler_master_mwe_white_550w_v2_mpe_5501_acabw_eu_1591141277_1.jpg"),
        Component(21, "Power Supply", "Thermaltake Toughpower GF1 750W", 8490.0, "https://regard-store.ru/upload/iblock/7fa/bwpfrcn5p62zeobcni04g3obc3a2z0q1.jpg"),
        Component(22, "Power Supply", "Be Quiet! Straight Power 11 850W", 13490.0, "https://al-fullsize.object.pscloud.io/files/alfa/messages/1/2/6/0/2/12602086-be-quiet-straight-power-11-850w.jpg"),

        // Graphics Cards
        Component(23, "Graphics Card", "NVIDIA GeForce RTX 3060", 34990.0, "https://avatars.mds.yandex.net/i?id=939cd6a7f1759a69914414136966169a7a0bb084-4316934-images-thumbs&n=13"),
        Component(24, "Graphics Card", "AMD Radeon RX 6600", 26990.0, "https://andpro.ru/upload/iblock/bb4/garn2duk1q5wh3f3xe4hil911t1cmtxx/62b26899_e05f_11ee_8148_001e67d1aaea_5de60844_0b9c_11ef_8148_001e67d1aaea.jpg"),
        Component(25, "Graphics Card", "NVIDIA GeForce RTX 3080", 79990.0, "https://cdna.artstation.com/p/assets/images/images/030/023/616/large/muhammx-alnazir-rtx3080-r1.jpg?1599371316"),
        Component(26, "Graphics Card", "AMD Radeon RX 6700 XT", 48990.0, "https://www.notebookcheck.net/fileadmin/Notebooks/AMD/RX_6700_XT/AMD_Radeon_RX_6700_XT_Graphics_Card_1.jpg"),

        // Cases
        Component(27, "Case", "NZXT H510", 4990.0, "https://newmart.ru/upload/iblock/13e/13e352796386e4c0ff6879bcf84bc3d4.jpg"),
        Component(28, "Case", "Cooler Master MasterBox Q300L", 4290.0, "https://avatars.mds.yandex.net/i?id=2b59d5ef26e546ed433429d383ee32fa_l-8000733-images-thumbs&n=13"),
        Component(29, "Case", "Corsair 4000D Airflow", 7490.0, "https://assets.corsair.com/image/upload/c_pad,q_auto,h_1024,w_1024,f_auto/products/Cases/base-4000d-airflow-config/Gallery/4000D_AF_WHITE_18.webp"),
        Component(30, "Case", "Fractal Design Meshify C", 8990.0, "https://avatars.mds.yandex.net/i?id=377640e421ac36c5135b0712756a068662aaa83d-5855963-images-thumbs&n=13"),

        // Storage
        Component(31, "HDD", "WD Blue 1TB HDD", 2990.0, "https://avatars.mds.yandex.net/i?id=d078fd11cf505f52de6b1883e8e1666862ff983e-10805353-images-thumbs&n=13"),
        Component(32, "SSD", "Samsung 970 EVO Plus 1TB SSD", 8490.0, "https://static.onlinetrade.ru/img/users_images/266927/b/ssd_disk_samsung_970_evo_m.2_1tb_pcie_gen_3.0_x4_v_nand_3bit_mlc_mz_v7e1t0bw_1572194528_1.jpg"),

        Component(33, "HDD","Seagate Barracuda 2TB", 5400.0,  "https://avatars.mds.yandex.net/i?id=7efa98fb7c98d8ef9bf820a5ec590a24_l-5878560-images-thumbs&n=13"),
        Component(34, "HDD", "Western Digital Blue 1TB", 2800.0,  "https://pc-1.ru/pic/medium/1062902.jpg"),
        Component(35, "SSD","Kingston A2000 500GB", 4650.0, "https://avatars.mds.yandex.net/i?id=ed72e3d7c376bfd85ff2eee126a96718_l-5233757-images-thumbs&n=13")


    )
}




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
        "Processor", "Motherboard", "RAM", "Power Supply",
        "Graphics Card", "Case", "HDD", "SSD"
    )

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Название заказа") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Описание заказа") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            componentTypes.forEach { type ->
                Text("$type:")
                ComponentDropdown(
                    type = type,
                    selectedComponent = selectedComponents[type],
                    onComponentSelected = { component ->
                        selectedComponents = selectedComponents.toMutableMap().apply {
                            put(type, component)
                        }
                        totalPrice = selectedComponents.values.sumOf { it.price }
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Общая сумма заказа: ${NumberFormat.getCurrencyInstance().format(totalPrice)}")

            Spacer(modifier = Modifier.height(16.dp))

            if (error.isNotEmpty()) {
                Text(text = error, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = {
                    if (name.isBlank() || description.isBlank() || selectedComponents.size < componentTypes.size) {
                        error = "Пожалуйста, заполните все поля и выберите все комплектующие."
                    } else {
                        val componentsList = selectedComponents.values.toList()
                        Log.d("addorder", "componentsList: $componentsList")

                        databaseHelper.insertOrder(
                            description = description,
                            name = name,
                            price = totalPrice,
                            imageUri = null,
                            idEmployee = null,
                            idClient = idClient,
                            idStatus = 1,
                            components = componentsList
                        )
                        onOrderAdded()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Добавить заказ")
            }
        }
    }
}


@Composable
fun ComponentDropdown(
    type: String,
    selectedComponent: Component?,
    onComponentSelected: (Component) -> Unit
) {

    val components = remember { getLocalComponents().filter { it.type == type } }
    var expanded by remember { mutableStateOf(false) }

    Box {
        Text(
            text = selectedComponent?.name ?: "Выбрать $type",
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
            components.forEach { component ->
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
                                modifier = Modifier.weight(1f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = NumberFormat.getCurrencyInstance().format(component.price),
                                modifier = Modifier.wrapContentWidth(Alignment.End)
                            )
                        }
                    }
                )
            }
        }
    }
}

