package com.markettwits.cloud_shop.api

interface LoadQuoteResult {

    fun <T : Any> map(mapper: Mapper<T>): T

    interface Mapper<T : Any> {

        fun mapSuccess(quote: String): T

        fun mapError(message: String): T
    }

    data class Success(private val quote: String) : LoadQuoteResult {
        override fun <T : Any> map(mapper: Mapper<T>): T {
            return mapper.mapSuccess(quote)
        }
    }

    data class Error(private val message: String) : LoadQuoteResult {
        override fun <T : Any> map(mapper: Mapper<T>): T {
            return mapper.mapError(message)
        }
    }
}

data class DataObjects(val id: Int, val name: String)

interface Repository {
    fun fetch(): List<LoadQuoteResult>
}

interface DataObjectsToDomainMapper : LoadQuoteResult.Mapper<LoadQuoteResult>
class DataObjectsToDomainMapperBase : DataObjectsToDomainMapper,
    LoadQuoteResult.Mapper<LoadQuoteResult> {
    override fun mapSuccess(quote: String): LoadQuoteResult = LoadQuoteResult.Success(quote)

    override fun mapError(message: String): LoadQuoteResult = LoadQuoteResult.Error(message)
}

class RepositoryBase(
    private val dataSource: List<DataObjects>,
    private val mapper: DataObjectsToDomainMapperBase
) : Repository {
    override fun fetch(): List<LoadQuoteResult> =
        try {
            dataSource.map { mapper.mapSuccess(it.name) }
        } catch (e: Exception) {
            listOf(mapper.mapError(e.message.toString()))
        }

}

