package com.example.final_project_assignment_hansenbillyramades.domain.repository

import com.example.final_project_assignment_hansenbillyramades.data.model.ProductDetailResponse
import com.example.final_project_assignment_hansenbillyramades.data.model.ProductResponse
import com.example.final_project_assignment_hansenbillyramades.data.source.network.UserRemoteDataSource
import com.example.final_project_assignment_hansenbillyramades.domain.model.Products

import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource
) : ProductRepository {
    override suspend fun getAllProducts(search: String?, limit: Int?): List<Products> {
        return remoteDataSource.getAllProducts(search, limit).data?.mapNotNull { it?.toProduct() } ?: emptyList()
    }

    override suspend fun getProductById(id: Int): Products {
        return remoteDataSource.getProductById(id).data!!.toProductDetail()
    }



    override suspend fun getProductByCategory(categoryName: String, search: String?): List<Products> {
        return remoteDataSource.getProductByCategory(categoryName, search).data
            ?.flatMap { dataItem -> val category = dataItem?.categories?.firstOrNull()?.ctName ?: "No Category"
                dataItem?.products.orEmpty().mapNotNull { it?.toProductByCategory(category) }
            }.orEmpty()
    }

    // mengdeklrasikan suspend yang dimana bahwa fungsi ini bisa berjalan secara asinkron
    // digunakan untuk mengolah data yang diperoleh dengan flatMap, yang akan meratakan list dari list yang ada.
// Artinya, jika ada list di dalam list, flatMap akan meratakan menjadi satu list.


    // mengembalikan list kosong jika orEmpty atau null

    // val category = dataItem?.categories?.firstOrNull()?.ctName ?: "No Category adalah
    // Di dalam blok flatMap, setiap item data (misalnya produk) diproses. dataItem?.categories?.firstOrNull()?.
// ctName mencoba mengakses nama kategori pertama dari item produk, jika ada.

    // nah yang dataitem di gunakan untuk mengakses data produk pada api, yang berisikan list, jika
    // jika list nyanull makan akan mengembalikan list kosong.

    // mapNotNull { it?.toProductByCategory(category) } adalah operasi untuk mengubah setiap produk
    // dalam daftar menjadi objek Products yang sesuai, dengan menggunakan kategori yang telah diambil sebelumnya.
    // Fungsi toProductByCategory sepertinya adalah ekstensi atau fungsi konversi untuk mengubah objek produk menjadi objek Products.

    //orEmpty di akhir, digunakan untuk handle jika list berdasrkan kaegori nya null maka akan
    // kembalikan list kosong

    // mengapa menggunakm mapnotNull karena Fungsi mapNotNull digunakan di sini untuk memetakan
// setiap elemen dalam list data menjadi objek Products (melalui ekstensi toProduct()) dan
// mengabaikan nilai null yang mungkin terjadi selama pemetaan.

    // jadi pada inti nya itu, sebelum kita nge mapping dari dri response nya ke domain, maka sudah di cek dulu
    // data nya null atau tidak, kalau null maka data di abaikan

    //kalau emptylist, jika daftar produk nya kosong ya kemnalikan saja list kosong

}

fun ProductResponse.Data.Product.toProductByCategory(category: String): Products {
    return Products(
        id = this.pdId ?: 0,
        name = this.pdName ?: "",
        price = this.pdPrice ?: 0,
        description = this.pdDescription ?: "",
        category = category,
        image = this.pdImageUrl ?: "",
        quantity = this.pdQuantity ?: 0,
        averageRating = this.totalAverageRating ?: 0.0,
        totalReviews = this.totalReviews ?: ""
    )
}


fun ProductResponse.Data.toProduct(): Products {
    return Products(
        id = this.pdId ?: 0,
        name = this.pdName ?: "",
        price = this.pdPrice ?: 0,
        description = this.pdDescription ?: "",
        category = this.categories?.firstOrNull()?.ctName ?: "No Category",
        image = this.pdImageUrl ?: "",
        quantity = this.pdQuantity ?: 0,
        averageRating = this.totalAverageRating ?: 0.0,
        totalReviews = this.totalReviews ?: ""
    )
}

fun ProductDetailResponse.Data.toProductDetail(): Products {
    return Products(
        id = this.pdId ?: 0,
        name = this.pdName ?: "",
        price = this.pdPrice ?: 0,
        description = this.pdDescription ?: "",
        category = this.categories?.firstOrNull()?.ctName ?: "No Category",
        image = this.pdImageUrl ?: "",
        quantity = this.pdQuantity ?: 0,
        averageRating = this.totalAverageRating ?: 0.0,
        totalReviews = this.totalReviews ?: ""
    )
}


