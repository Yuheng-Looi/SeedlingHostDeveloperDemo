package com.seedling.demo.hostdeveloperdemo

class CardList {

    val cards: MutableList<Card> = mutableListOf()

    fun addCard(cardNumber: String, cardHolderName: String, cardID: String): Boolean {
        if (cards.any { it.cardNumber == cardNumber }) {
            val card = Card(cardNumber, cardHolderName, cardID)
            cards.add(card)
            return true
        }
        return false
    }

    fun initCardList() {
        addCard("Card 1", "Name1", "visa")
        addCard("Card 2", "Yu Heng", "tng")
        addCard("Card 3", "Henry", "any")
    }

}
