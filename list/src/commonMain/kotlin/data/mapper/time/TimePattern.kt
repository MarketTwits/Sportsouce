package data.mapper.time

interface TimePattern {
    fun map(): String
    object Instance : TimePattern{
        override fun map() = "MMMM dd, yyyy"

    }

    object FullWithDots : TimePattern {
        override fun map() = "dd.MM.yyyy"
    }

    object Remote : TimePattern {
        override fun map() = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    }
}