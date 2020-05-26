package ru.app.pharmacy.models

data class Category(
    val id: Int,
    val title: String,
    var parentId: Int? = null
)

data class Group(
    val id: Int,
    val title: String,
    val categories: List<Int>
)

data class CategoryResponse(
    val groups: List<Group>,
    val categories: List<Category>
)

fun CategoryResponse.toCategory(): List<Category> {
    val res = mutableListOf<Category>()
    val groups = mutableListOf<Category>()
    val categories = mutableMapOf<Int, Category>()

    for (i in this.categories.indices)
        categories[this.categories[i].id] = this.categories[i]

    for (i in this.groups.indices) {
        groups.add(
            Category(
                id = this.groups[i].id,
                title = this.groups[i].title
            )
        )
        val ids = this.groups[i].categories
        for (j in ids.indices) {
            categories[ids[j]]?.let {
                it.parentId = this.groups[i].id
                res.add(it)
            }
        }
    }
    res.addAll(groups)
    return res
}