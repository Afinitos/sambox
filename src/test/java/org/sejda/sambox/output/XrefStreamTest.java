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
package org.sejda.sambox.output;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.sejda.io.SeekableSource;
import org.sejda.sambox.cos.COSArray;
import org.sejda.sambox.cos.COSDictionary;
import org.sejda.sambox.cos.COSInteger;
import org.sejda.sambox.cos.COSName;
import org.sejda.sambox.xref.CompressedXrefEntry;
import org.sejda.sambox.xref.XrefEntry;

/**
 * @author Andrea Vacondio
 *
 */
public class XrefStreamTest
{
    private PDFWriteContext context;

    @Before
    public void setUp()
    {
        context = new PDFWriteContext(null);
        context.addWritten(CompressedXrefEntry.compressedEntry(2, 4, 1));
        context.addWritten(XrefEntry.inUseEntry(4, 256, 0));
    }

    @Test
    public void keysArePopulated() throws IOException
    {
        try (XrefStream victim = new XrefStream(new COSDictionary(), context))
        {
            assertEquals(COSName.XREF, victim.getCOSName(COSName.TYPE));
            assertEquals(5, victim.getLong(COSName.SIZE));
            COSArray index = (COSArray) victim.getItem(COSName.INDEX);
            assertNotNull(index);
            assertEquals(2, ((COSInteger) index.getObject(0)).intValue());
            assertEquals(3, ((COSInteger) index.getObject(1)).intValue());
            COSArray w = (COSArray) victim.getItem(COSName.W);
            assertEquals(1, ((COSInteger) w.getObject(0)).intValue());
            assertEquals(2, ((COSInteger) w.getObject(1)).intValue());
            assertEquals(2, ((COSInteger) w.getObject(2)).intValue());
            assertEquals(victim.getLong(COSName.DL), victim.getUnfilteredLength());
        }
    }

    @Test
    public void streamIsPopulated() throws IOException
    {
        try (XrefStream victim = new XrefStream(new COSDictionary(), context))
        {
            SeekableSource source = victim.getUnfilteredSource();
            assertEquals(0b00000010, source.read());
            assertEquals(0b00000000, source.read());
            assertEquals(0b00000100, source.read());
            assertEquals(0b00000000, source.read());
            assertEquals(0b00000001, source.read());

            assertEquals(0b00000000, source.read());
            assertEquals(0b00000000, source.read());
            assertEquals(0b00000011, source.read());
            assertEquals(0b00000000, source.read());
            assertEquals(0b00000000, source.read());

            assertEquals(0b00000001, source.read());
            assertEquals(0b00000001, source.read());
            assertEquals(0b00000000, source.read());
            assertEquals(0b00000000, source.read());
            assertEquals(0b00000000, source.read());
        }
    }
}
