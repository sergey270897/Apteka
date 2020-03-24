package ru.app.apteka.utils

import org.json.JSONObject
import ru.app.apteka.models.Category

object Utils {
    fun json2Category(obj: JSONObject): List<Category> {
        val res = mutableListOf<Category>()
        val groups = mutableListOf<Category>()
        val categories = mutableMapOf<Int, Category>()

        val cat = obj.getJSONArray("categories")
        for (i in 0 until cat.length()) {
            categories[cat.getJSONObject(i).getInt("id")] = Category(
                id = cat.getJSONObject(i).getInt("id"),
                title = cat.getJSONObject(i).getString("title")
            )
        }

        val gr = obj.getJSONArray("groups")
        for (i in 0 until gr.length()) {
            groups.add(
                Category(
                    id = gr.getJSONObject(i).getInt("id"),
                    title = gr.getJSONObject(i).getString("title")
                )
            )
            val ids = gr.getJSONObject(i).getJSONArray("categories")
            for (j in 0 until ids.length()) {
                categories[ids.getInt(j)]?.let {
                    it.parentId = gr.getJSONObject(i).getInt("id")
                    res.add(it)
                }
            }
        }

        res.addAll(groups)
        return res
    }
}