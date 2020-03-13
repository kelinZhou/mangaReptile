package com.neuifo.domain.modeltype


import com.google.gson.annotations.JsonAdapter
import com.neuifo.domain.utils.EnumDeserializers

/**
 * Created by neuifo on 2017/9/12.
 */
@JsonAdapter(BooleanEntity.BooleanEntityDeserializer::class)
enum class BooleanEntity private constructor(val `val`: Int) : EnumDeserializers.SerializableEnum {

    TRUE(1), FALSE(0);

    val booleanValue: Boolean
        get() = TRUE == this

    override fun getId(): Int {
        return `val`
    }

    class BooleanEntityDeserializer : EnumDeserializers.IntEnumEnumSerializer<BooleanEntity>() {

        override fun getSerializableValues(): Array<BooleanEntity> {
            return BooleanEntity.values()
        }
    }

    companion object {

        fun from(i: Int): BooleanEntity {
            return if (i == TRUE.`val`) {
                TRUE
            } else FALSE
        }

        fun from(value: Boolean): BooleanEntity {
            return if (value == TRUE.booleanValue) {
                TRUE
            } else FALSE
        }
    }
}
