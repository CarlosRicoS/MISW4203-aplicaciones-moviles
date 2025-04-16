package co.edu.uniandes.miso.vinilos.view

sealed class DrawerItem {
    data class MenuItem(val id: Int, val iconRes: Int, val title: String) : DrawerItem()
    object Divider : DrawerItem()
}