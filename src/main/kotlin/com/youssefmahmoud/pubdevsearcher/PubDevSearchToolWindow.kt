package com.youssefmahmoud.pubdevsearcher

import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.components.JBScrollPane
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JTextField
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.GridBagLayout
import java.awt.GridBagConstraints
import java.awt.BorderLayout

class PubDevSearchToolWindow(toolWindow: ToolWindow) {
    val content: JPanel = JPanel(BorderLayout())
    private val project = toolWindow.project
    private val labelPanel: JPanel = JPanel(GridBagLayout())
    private val containerPanel: JPanel = JPanel(FlowLayout(FlowLayout.LEFT))
    private val scrollPane: JBScrollPane
    private val loadMoreButton: JButton = JButton("Load More")
    private var nextUrl: String? = null

    init {
        val loadMoreConstraints = GridBagConstraints()
        loadMoreConstraints.gridx = 0
        loadMoreConstraints.gridy = GridBagConstraints.RELATIVE
        loadMoreConstraints.anchor = GridBagConstraints.WEST // Align button to the left
        containerPanel.add(labelPanel)
        scrollPane = JBScrollPane(containerPanel)
        scrollPane.preferredSize = Dimension(400, 300)
        scrollPane.verticalScrollBarPolicy = JBScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED

        val row = JPanel(FlowLayout(FlowLayout.LEFT))
        val textfield = JTextField(20)
        textfield.preferredSize = Dimension(Int.MAX_VALUE, 30)
        textfield.maximumSize = Dimension(Int.MAX_VALUE, 30)
        row.add(textfield)

        val button = JButton("Search")
        button.addActionListener {
            val search = textfield.text
            val response = HttpHelper.getPackages(search)
            if (response != null) {
                labelPanel.removeAll()
                for (packageModel in response.packages) {
                    addLabel(packageModel.name)
                }
                nextUrl = response.next
                loadMoreButton.isVisible = nextUrl != null
                // Ensure the button visibility changes are respected
                labelPanel.add(loadMoreButton, loadMoreConstraints)
                labelPanel.revalidate()
                labelPanel.repaint()
            }
        }
        row.add(button)

        content.add(row, BorderLayout.NORTH)
        content.add(scrollPane, BorderLayout.CENTER)

        // Add Load More button to the bottom of the labelPanel
        loadMoreButton.addActionListener {
            if (nextUrl != null) {
                val response = HttpHelper.getPackagesWithUrl(nextUrl!!)
                if (response != null) {
                    for (packageModel in response.packages) {
                        addLabel(packageModel.name)
                    }
                    nextUrl = response.next
                    loadMoreButton.isVisible = nextUrl != null
                    labelPanel.add(loadMoreButton, loadMoreConstraints)
                    labelPanel.revalidate()
                    labelPanel.repaint()
                }
            }
        }
        labelPanel.add(loadMoreButton, loadMoreConstraints)
        loadMoreButton.isVisible = false // Initialize as invisible

        toolWindow.component.add(content)
    }

    private fun addLabel(text: String) {
        val label = PubDevLabelComponent(text, project)
        val labelConstraints = GridBagConstraints()
        labelConstraints.gridx = 0
        labelConstraints.gridy = GridBagConstraints.RELATIVE
        labelConstraints.anchor = GridBagConstraints.WEST
        labelPanel.add(label, labelConstraints)
        labelPanel.revalidate()
        labelPanel.repaint()
    }
}
