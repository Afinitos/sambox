/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.pdfbox.cos;

import java.io.IOException;
import java.util.Optional;

/**
 * A COSBase that will be written as direct object
 * 
 * @author Andrea Vacondio
 */
public final class DirectCOSObject extends COSBase
{

    private COSBase baseObject;

    private DirectCOSObject(COSBase wrapped)
    {
        this.baseObject = Optional.ofNullable(wrapped).orElse(COSNull.NULL);
    }

    @Override
    public COSBase getCOSObject()
    {
        return baseObject;
    }
    @Override
    public void accept(COSVisitor visitor) throws IOException
    {
        baseObject.accept(visitor);
    }

    /**
     * Factory method for an object that will be written as a direct object.
     * 
     * @param wrapped
     * @return the new instance
     */
    public static DirectCOSObject asDirectObject(COSBase wrapped)
    {
        return new DirectCOSObject(wrapped.getCOSObject());
    }
}
