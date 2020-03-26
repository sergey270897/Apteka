package ru.app.apteka.utils

import org.json.JSONObject
import ru.app.apteka.models.Category
import ru.app.apteka.models.Medicine

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

    fun json2Medicine(obj: JSONObject):List<Medicine>{
        val arr = obj.getJSONArray("products")
        val res = mutableListOf<Medicine>()
        for(i in 0 until arr.length()){
            val med = Medicine(
                id = arr.getJSONObject(i).getInt("id"),
                title = arr.getJSONObject(i).getString("title"),
                categoryId = arr.getJSONObject(i).getInt("categoryId"),
                categoryName = arr.getJSONObject(i).getString("categoryName"),
                image = arr.getJSONObject(i).getString("image"),
                price = arr.getJSONObject(i).getDouble("price").toFloat(),
                rating = arr.getJSONObject(i).getDouble("rating").toFloat(),
                available = arr.getJSONObject(i).getBoolean("available")
            )
            res.add(med)
        }
        return res
    }
}