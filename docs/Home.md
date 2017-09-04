# Documentation

Project code is placed inside `src/main/javasistemidistribuiti/sistemidistribuiti/uno`.

## Packages

- App.java (entry point)
- bean
- exception
- listener
- model
- rmi
- utils
- view
- workflow

### Bean

Classes in `bean` package are responsible for configuration (ConfigBean).

### Exception

In `exception` package we find `NextPlayerNotFoundException`, thrown when the following player is not reachable.

### Listener

Here `DataReceiverListener` used for RMI callbacks.

### Model

In `model` there are following subfolder:
- card
- game
- player

#### Card

`card` contains 3 classes:

 - CARD_COLOR
 - CARD_TYPE_ENUM
 - SPECIAL_CARD_TYPE

These classes are useful for a fully featured *Uno card* object, with properties like color, type and/or number.

`UnoCard` is an abstract class for Card modelling, with following properties:

 - Color (CARD_COLOR)
 - CardType (CARD_TYPE_ENUM)

Inside `card` package is also available a sub package `impl`, which provides UnoCard's implementations.

 - `NumberCard` is a subclass of `UnoCard` and it is specialised for simple number cards
 - `SpecialCard` is a subclass of `UnoCard` and it is specialised for special cards like draw, color change, skip.