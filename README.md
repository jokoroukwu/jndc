# JNDC

A lightweight open-source Java library for serialization/deserialization of APTRA™ Advance NDC messages.

This library was created by me as a side project of my full-time job and initially supported features
I needed at the time.<br> Now I actively add new features to it.

Contribution is welcome :)

## Features

(De)serialization of the following messages is currently supported:

Terminal to Central:

- Transaction Request Message
- Solicited Ready 'B' Status Message
- (Un)solicited Card Reader/Writer Status Message
- Basic/Extended Solicited Send Supply Counters Response Message

Central to Terminal:

- Transactions Reply Command
- EMV Configuration Messages
- Extended Encryption Key Change
- State Tables Load
- Screen/Keyboard Data Load
- Enhanced Configuration Parameters Load
- FIT Data Load
- Configuration ID Number Load
- Date and Time Load
- Terminal Commands

Usage examples can be found [here](examples/src/main/java/io/github/jokoroukwu/examples)

## Licence

Copyright 2022 John Okoroukwu

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
License. You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "
AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
language governing permissions and limitations under the License.