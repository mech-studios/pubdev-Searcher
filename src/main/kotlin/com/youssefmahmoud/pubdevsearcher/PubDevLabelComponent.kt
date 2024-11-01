// PubDevLabelComponent.kt
package com.youssefmahmoud.pubdevsearcher

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.IconLoader
import com.intellij.util.ui.JBUI.insets
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.Icon

class PubDevLabelComponent(val text: String, private val project: Project) : JPanel(GridBagLayout()) {
    private val heartIcon: Icon = IconLoader.getIcon("/heart_unselected.svg", javaClass)
    private val selectedHeartIcon: Icon = IconLoader.getIcon("/heart_selected.svg", javaClass)

    init {
        val isFavourite = FavouritesManager.instance.isFavourite(text)
        val heartPanel = JPanel(BorderLayout())
        val heartLabel = JLabel(if (isFavourite) selectedHeartIcon else heartIcon)
        heartPanel.add(heartLabel, BorderLayout.CENTER)

        // Heart icon toggle on click
        heartPanel.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if (FavouritesManager.instance.isFavourite(text)) {
                    FavouritesManager.instance.removeFavourite(text)
                    heartLabel.icon = heartIcon
                } else {
                    FavouritesManager.instance.addFavourite(text)
                    heartLabel.icon = selectedHeartIcon
                }
                heartPanel.revalidate()
                heartPanel.repaint()
            }
        })

        // Label and add button setup
        val label = JLabel(text)
        val addButton = JButton("Add to Project")
        addButton.addActionListener {
            Utilities.runCommandInJetBrainsTerminal(project, listOf("flutter pub add $text", "flutter pub get"))
        }

        // Layout and constraints for components
        val heartPanelConstraints = GridBagConstraints().apply {
            gridx = 0
            anchor = GridBagConstraints.WEST
            insets = insets(5)
        }
        val labelConstraints = GridBagConstraints().apply {
            gridx = 1
            anchor = GridBagConstraints.WEST
            insets = insets(5)
        }
        val buttonConstraints = GridBagConstraints().apply {
            gridx = 2
            anchor = GridBagConstraints.WEST
            insets = insets(5)
        }

        add(heartPanel, heartPanelConstraints)
        add(label, labelConstraints)
        add(addButton, buttonConstraints)
    }
}
