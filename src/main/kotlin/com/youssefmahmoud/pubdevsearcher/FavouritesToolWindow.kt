package com.youssefmahmoud.pubdevsearcher

import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.components.JBScrollPane
import java.awt.FlowLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JPanel

class FavouritesToolWindow(toolWindow: ToolWindow) : FavouritesChangeListener {
    private val content: JPanel = JPanel(GridBagLayout())
    private  val contentParent: JPanel = JPanel(FlowLayout(FlowLayout.LEFT))
    val contentScrollPane: JBScrollPane = JBScrollPane(contentParent)
    private val project = toolWindow.project
    private val labelConstraints = GridBagConstraints()

    init {
        labelConstraints.gridx = 0
        labelConstraints.gridy = GridBagConstraints.RELATIVE
        labelConstraints.anchor = GridBagConstraints.NORTHWEST
        contentParent.add(content)
        FavouritesManager.instance.addListener(this)
        val favourites = FavouritesManager.instance.getFavourites()

        for (favourite in favourites) {
            val label = PubDevLabelComponent(favourite, project)
            content.add(label, labelConstraints)
        }
    }

    override fun onFavouriteAdded(favourite: String) {
        val label = PubDevLabelComponent(favourite, project)
        content.add(label, labelConstraints)
        content.revalidate()
        content.repaint()
    }

    override fun onFavouriteRemoved(favourite: String) {
        val components = content.components
        for (component in components) {
            if (component is PubDevLabelComponent && component.text == favourite) {
                content.remove(component)
                if (!isToolWindowOpen()) {
                    content.revalidate()
                    content.repaint()
                }
                break
            }
        }
    }

    private fun isToolWindowOpen(): Boolean {
        val toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Favourites")
        return toolWindow?.isVisible ?: false
    }
}
