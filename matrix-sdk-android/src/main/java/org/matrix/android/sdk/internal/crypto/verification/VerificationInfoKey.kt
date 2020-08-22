/*
 * Copyright 2019 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.matrix.android.sdk.internal.crypto.verification

/**
 * Sent by both devices to send their ephemeral Curve25519 public key to the other device.
 */
internal interface VerificationInfoKey : VerificationInfo<ValidVerificationInfoKey> {
    /**
     * The device’s ephemeral public key, as an unpadded base64 string
     */
    val key: String?

    override fun asValidObject(): ValidVerificationInfoKey? {
        val validTransactionId = transactionId?.takeIf { it.isNotEmpty() } ?: return null
        val validKey = key?.takeIf { it.isNotEmpty() } ?: return null

        return ValidVerificationInfoKey(
                validTransactionId,
                validKey
        )
    }
}

internal interface VerificationInfoKeyFactory {
    fun create(tid: String, pubKey: String): VerificationInfoKey
}

internal data class ValidVerificationInfoKey(
        val transactionId: String,
        val key: String
)