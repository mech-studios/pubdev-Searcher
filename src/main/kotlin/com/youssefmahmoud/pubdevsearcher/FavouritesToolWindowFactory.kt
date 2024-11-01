package com.youssefmahmoud.pubdevsearcher

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory

class FavouritesToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val myToolWindow = FavouritesToolWindow(toolWindow)
        val contentFactory = ContentFactory.getInstance()
        val content = contentFactory.createContent(myToolWindow.contentScrollPane, "", false)
        toolWindow.contentManager.addContent(content)
        val icon = IconLoader.getIcon("/main_window_icon.svg", javaClass)
        toolWindow.setIcon(icon)
    }
}