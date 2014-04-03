/*
 * Copyright 2014 Jin Kwon.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.github.jinahya.io.bit;


import com.google.inject.Provider;
import javax.inject.Inject;


/**
 *
 * @author Jin Kwon
 */
public class BitInputProvider implements Provider<BitInput> {


    @Override
    public BitInput get() {

        //return new BitInput(new ByteInputMock());
        return new BitInput(input);
    }


    @Inject
    private ByteInput input;
}

