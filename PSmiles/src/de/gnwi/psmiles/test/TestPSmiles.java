/**
 * PSmiles (Particle SMILES)
 * Copyright (C) 2018  Achim Zielesny (achim.zielesny@googlemail.com)
 * 
 * Source code is available at <https://github.com/zielesny/PSMILES>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.gnwi.psmiles.test;

import java.util.Arrays;
import java.util.HashMap;
import junit.framework.TestCase;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import de.gnwi.psmiles.IPointInSpace;
import de.gnwi.psmiles.PSmilesInner;
import de.gnwi.psmiles.PSmiles;
import de.gnwi.psmiles.MessagePSmiles;
import de.gnwi.psmiles.ParticleFrequency;
import de.gnwi.psmiles.PointInSpace;

/**
 * Test class for class PSmiles
 *
 * @author Mirco Daniel, Achim Zielesny
 */
public class TestPSmiles extends TestCase {

    // <editor-fold defaultstate="collapsed" desc="Property test methods">
    /**
     * Test of property getInputStructure
     */
    public void testGetInputStructure() {
        String tmpInputStructure;
        String tmpResultString;
        String tmpExpectString;

        PSmiles tmpPSmiles = new PSmiles(null);
        tmpInputStructure = tmpPSmiles.getInputStructure();
        assertNull("Test1.1", tmpInputStructure);

        tmpPSmiles = new PSmiles("");
        tmpInputStructure = tmpPSmiles.getInputStructure();
        assertTrue("Test1.2", tmpInputStructure.equals(""));

        tmpPSmiles = new PSmiles("A*-A");
        tmpInputStructure = tmpPSmiles.getInputStructure();
        assertTrue("Test1.3", tmpInputStructure.equals("A*-A"));

        tmpPSmiles = new PSmiles("A-B-C");
        tmpInputStructure = tmpPSmiles.getInputStructure();
        assertTrue("Test1.4", tmpInputStructure.equals("A-B-C"));

        tmpPSmiles = new PSmiles("(A)");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingConnection");
        assertEquals("Test1.5a", tmpResultString, tmpExpectString);

        tmpPSmiles = new PSmiles("3(A)");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidParticlePriorNormalBracket");
        assertEquals("Test1.5b", tmpResultString, tmpExpectString); // An illegal structure 

        tmpPSmiles = new PSmiles("(A[1]-B)(B-A[1])");
        tmpResultString = tmpPSmiles.getErrorMessage();
        assertNull("Test1.5c", tmpResultString);

        tmpPSmiles = new PSmiles("(A-B)A");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidParticleAfterNormalClosingBracket");
        assertEquals("Test1.5d", tmpExpectString, tmpResultString);

        tmpPSmiles = new PSmiles("(A-B)-A");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidParticleAfterNormalClosingBracket");
        assertEquals("Test1.5e", tmpExpectString, tmpResultString);

        tmpPSmiles = new PSmiles("(A-B)(D-E)-A");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidParticleAfterNormalClosingBracket");
        assertEquals("Test1.5f", tmpExpectString, tmpResultString);

        tmpPSmiles = new PSmiles("(A)(B)");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingConnection");
        assertEquals("Test1.5g", tmpResultString, tmpExpectString);

        tmpPSmiles = new PSmiles("(A[1]-B-C[1])(B-A)");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingConnection");
        assertEquals("Test1.5h", tmpResultString, tmpExpectString);

        tmpPSmiles = new PSmiles("A<B>");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidCharacterPriorAngularBracket");
        assertEquals("Test1.5i", tmpResultString, tmpExpectString);

        tmpPSmiles = new PSmiles("<A>B");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidParticleAfterAngleClosingBracket");
        assertEquals("Test1.5j", tmpResultString, tmpExpectString);

        tmpPSmiles = new PSmiles("<A>B<A>");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidCharacterPriorAngularBracket");
        assertEquals("Test1.5k", tmpResultString, tmpExpectString);

        tmpPSmiles = new PSmiles("<A>2<A>B");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidParticleAfterAngleClosingBracket");
        assertEquals("Test1.5l", tmpResultString, tmpExpectString);

        tmpPSmiles = new PSmiles("<A>2<>");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.EmptyAngleBrackets");
        assertEquals("Test1.5m", tmpResultString, tmpExpectString);

        tmpPSmiles = new PSmiles("3<A>2<A><C-C-C>50<D>");
        tmpResultString = tmpPSmiles.getErrorMessage();
        assertNull("Test1.5n", tmpResultString);

        tmpPSmiles = new PSmiles("A B");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidWhiteSpace");
        assertEquals("Test1.5o", tmpResultString, tmpExpectString);

        tmpPSmiles = new PSmiles("'1'-B-B-C-D'2'");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidFirstCharacterOfStructure");
        assertEquals("Test1.6a", tmpResultString, tmpExpectString);

        tmpPSmiles = new PSmiles("3'1'A-B'2'");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidPositionOfBackboneIndex");
        assertEquals("Test1.6b", tmpResultString, tmpExpectString);

        tmpPSmiles = new PSmiles("A('1'A-B'2')");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidPositionOfBackboneIndex");
        assertEquals("Test1.6c", tmpResultString, tmpExpectString);

        tmpPSmiles = new PSmiles("A-A'1'-B-{A[HEAD]-B[TAIL]}'2'");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidPositionOfBackboneIndex");
        assertEquals("Test1.6d", tmpResultString, tmpExpectString);

        tmpPSmiles = new PSmiles("A'1'-A-'2'B");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidPositionOfBackboneIndex");
        assertEquals("Test1.6e", tmpResultString, tmpExpectString);

        tmpPSmiles = new PSmiles("A-A''-B");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.IllegalBackboneIndexFormat");
        assertEquals("Test1.6f", tmpResultString, tmpExpectString);

        tmpPSmiles = new PSmiles("A-A'A'-B");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.TooLessBackboneindex");
        assertEquals("Test1.6g", tmpResultString, tmpExpectString);
        
        tmpPSmiles = new PSmiles("H2O'1'Bu'2'");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingAConnectionAfterBackboneIndex");
        assertEquals("Test1.6h", tmpResultString, tmpExpectString);
        
        tmpPSmiles = new PSmiles("A'1'-B'2'");
        tmpResultString = tmpPSmiles.getErrorMessage();
        assertNull("Test1.6i", tmpResultString);
        
        tmpPSmiles = new PSmiles("A-0A");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.IllegalFrequency");
        assertEquals("Test1.6j", tmpResultString, tmpExpectString);
        
        tmpPSmiles = new PSmiles("A'0'-A'1'");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.ZeroInBackboneindex");
        assertEquals("Test1.6k", tmpResultString, tmpExpectString);
        
        tmpPSmiles = new PSmiles("10A'1'-B-C'2'");
        tmpResultString = tmpPSmiles.getErrorMessage();
        assertNull("Test1.6L", tmpResultString);
        
        tmpPSmiles = new PSmiles("{A'1'-A'2'}");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.BackboneIndexInMonomer");
        assertEquals("Test1.6m", tmpResultString, tmpExpectString);
        
        tmpPSmiles = new PSmiles("E-Cys-3{A");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingClosingCurlyBracket");
        assertEquals("Test1.6n", tmpResultString, tmpExpectString);
        
        tmpPSmiles = new PSmiles("<A-B-C><E-Cys-3{A>");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingClosingCurlyBracket");
        assertEquals("Test1.6o", tmpResultString, tmpExpectString);
    }

    /**
     * Test of property isValid
     */
    public void testIsValid() {
        PSmiles tmpPSmiles = new PSmiles(null);
        assertFalse("Test2.1", tmpPSmiles.isValid());

        tmpPSmiles = new PSmiles("");
        assertFalse("Test2.2", tmpPSmiles.isValid());

        tmpPSmiles = new PSmiles("A*-A");
        assertFalse("Test2.3", tmpPSmiles.isValid());

        tmpPSmiles = new PSmiles("A-B-C");
        assertTrue("Test2.4", tmpPSmiles.isValid());

        tmpPSmiles = new PSmiles("A<B>");
        assertFalse("Test2.5", tmpPSmiles.isValid());

        tmpPSmiles = new PSmiles("<A>B");
        assertFalse("Test2.6", tmpPSmiles.isValid());

        tmpPSmiles = new PSmiles("<A>B<A>");
        assertFalse("Test2.7", tmpPSmiles.isValid());

        tmpPSmiles = new PSmiles("<A>2<A>B");
        assertFalse("Test2.8", tmpPSmiles.isValid());

        tmpPSmiles = new PSmiles("<A>2<>");
        assertFalse("Test2.9", tmpPSmiles.isValid());

        tmpPSmiles = new PSmiles("3<A>2<A><C-C-C>50<D>");
        assertTrue("Test2.10", tmpPSmiles.isValid());

        tmpPSmiles = new PSmiles("1A-1A-1A");
        assertTrue("Test2.11a", tmpPSmiles.isValid());

        tmpPSmiles = new PSmiles("3A");
        assertTrue("Test2.11b", tmpPSmiles.isValid());

        tmpPSmiles = new PSmiles("A-3B");
        assertTrue("Test2.11c", tmpPSmiles.isValid());

        tmpPSmiles = new PSmiles("3A-B");
        assertTrue("Test2.11d", tmpPSmiles.isValid());

        tmpPSmiles = new PSmiles("4A-B-B-4C");
        assertTrue("Test2.11e", tmpPSmiles.isValid());

        tmpPSmiles = new PSmiles("{A[HEAD]-B-B-3C[TAIL]}");
        assertTrue("Test2.12a", tmpPSmiles.isValid());

        tmpPSmiles = new PSmiles("{A[HEAD]-B-2C[TAIL]}{A[HEAD]-B-2C[TAIL]}");
        assertTrue("Test2.12b", tmpPSmiles.isValid());

        tmpPSmiles = new PSmiles("3{A-B[HEAD]-B-3C[TAIL]}");
        assertTrue("Test2.12c", tmpPSmiles.isValid());

        tmpPSmiles = new PSmiles("Y-2{A-B[HEAD]-B-3C[TAIL]}-Z");
        assertTrue("Test2.12d", tmpPSmiles.isValid());

        tmpPSmiles = new PSmiles("{C[TAIL]-A[HEAD]-B}-{E-D[HEAD]-F[TAIL]}-G");
        assertTrue("Test2.12e", tmpPSmiles.isValid());

        tmpPSmiles = new PSmiles("{C[TAIL]-A[HEAD]-B}{E-D[HEAD]-F[TAIL]}-G");
        assertTrue("Test2.12f", tmpPSmiles.isValid());

        tmpPSmiles = new PSmiles("{C[TAIL]-A[HEAD]-B}2{E-D[HEAD]-F[TAIL]}-G");
        assertTrue("Test2.12f", tmpPSmiles.isValid());

        tmpPSmiles = new PSmiles("{A[HEAD]-B-2C[TAIL]}3{A[HEAD]-2B[TAIL]}{A[HEAD]-B-2C[TAIL]}");
        assertTrue("Test2.12g", tmpPSmiles.isValid());

        tmpPSmiles = new PSmiles("A-B(C)-D");
        assertTrue("Test2.13a", tmpPSmiles.isValid());

        tmpPSmiles = new PSmiles("A-B-C(D)");
        assertTrue("Test2.13b", tmpPSmiles.isValid());

        tmpPSmiles = new PSmiles("A-B(C1)(C2-D)(C3-D)-D");
        assertTrue("Test2.13c", tmpPSmiles.isValid());

        tmpPSmiles = new PSmiles("A-B(C1)-C(D1-E)-D2");
        assertTrue("Test2.13d", tmpPSmiles.isValid());

        tmpPSmiles = new PSmiles("A-B(C1(D1)(D1)-D1)(C2-D1(E1)-E1)(D1-E)-F");
        assertTrue("Test2.13e", tmpPSmiles.isValid());

        tmpPSmiles = new PSmiles("2{A[HEAD]-B(C)-D[TAIL]}-3{A[HEAD]-B(C)-D[TAIL]}");
        assertTrue("Test2.13f", tmpPSmiles.isValid());

        String tmpStructure = "MeAcNHBB[4](MeSHSS[1])-MeAcNHBB-MeAcNHPD2(Me-HAcN)-MeAcNHBB(PrOH)-MeAcNHBB(MeSHSS[2])-MeAcNHBB(Pr)-MeAcNHBB-MeAcNHBB-MeAcNHBB(PrOH)-MeAcNHBB(MeSHSS[3])-MeAcNHBB(AcNH2)-MeAcNHBB(PrOH)-AzolidBB-MeAcNHBB-MeAcNHBB(MeSHSS[1])-MeAcNHBB(PrOH)-MeAcNHBB(MeSHSS[2])-MeAcNHBB(MeOH)-MeAcNHPD1(Me-Pyrrole-Ph)-AzolidPD1-MeAcNHPD1(Pr)-MeAcNHBB(MeSHSS[3])-MeAcNHBB(PrOH)-MeAcNHPD3(Pr-GuanidineP)-MeAcNHBB(AcNH2)-MeAcNHBB-MeAcNHPD1(Me-Pr)-AzolidPD1-MeAcNHPD1[4](Pr)";
        tmpPSmiles = new PSmiles(tmpStructure);
        assertTrue("Test2.14", tmpPSmiles.isValid());

        tmpStructure = "<A> <B>";
        tmpPSmiles = new PSmiles(tmpStructure);
        assertTrue("Test2.15", tmpPSmiles.isValid());

        tmpStructure = "<A>\n<B>";
        tmpPSmiles = new PSmiles(tmpStructure);
        assertTrue("Test2.16", tmpPSmiles.isValid());

        tmpStructure = "<(A-B-C-D)(A-B-C-D)>";
        tmpPSmiles = new PSmiles(tmpStructure);
        assertFalse("Test2.17", tmpPSmiles.isValid());
        
        tmpStructure = "<(A-B[1]-C-D)(A-B-C[1]-D)>";
        tmpPSmiles = new PSmiles(tmpStructure);
        assertTrue("Test2.18", tmpPSmiles.isValid());
        
        tmpStructure = "<(A-B[2]-C-D)(A-B-C[2]-D)>";
        tmpPSmiles = new PSmiles(tmpStructure);
        assertTrue("Test2.19", tmpPSmiles.isValid());
        
        tmpStructure = "<(A-B[1]-C-D)(A-B-C[1]-D)><(A-B[2]-C-D)(A-B-C[2]-D)>";
        tmpPSmiles = new PSmiles(tmpStructure);
        assertTrue("Test2.20", tmpPSmiles.isValid());

        tmpStructure = "<(A(B[1])-C(D[2])-E(F[1])-G(H[3])-I)(A(B[2])-C(D[3])-E)>";
        tmpPSmiles = new PSmiles(tmpStructure);
        assertTrue("Test2.21", tmpPSmiles.isValid());

        tmpStructure = "<(A(B[3])-C(D[4])-E(F[3])-G(H[5])-I)(A(B[4])-C(D[5])-E)>";
        tmpPSmiles = new PSmiles(tmpStructure);
        assertTrue("Test2.22", tmpPSmiles.isValid());
        
        tmpStructure = "<(A(B[1])-C(D[2])-E(F[1])-G(H[3])-I)(A(B[2])-C(D[3])-E)><(A(B[3])-C(D[4])-E(F[3])-G(H[5])-I)(A(B[4])-C(D[5])-E)>";
        tmpPSmiles = new PSmiles(tmpStructure);
        assertTrue("Test2.23", tmpPSmiles.isValid());
        
        // There MUST be a monomer name after monomer start label '#'
        tmpStructure = "A-#-B";
        tmpPSmiles = new PSmiles(tmpStructure);
        assertFalse("Test2.24", tmpPSmiles.isValid());

        // A monomer name MUST begin with an upper Character, i.e. "#MyMonomer"
        tmpStructure = "A-#myMonomer-B";
        tmpPSmiles = new PSmiles(tmpStructure);
        assertFalse("Test2.25", tmpPSmiles.isValid());
    }

    /**
     * Test of property errorMessage
     */
    public void testErrorMessage() {
        // <editor-fold defaultstate="collapsed" desc="Simple checks">
        PSmiles tmpPSmiles = new PSmiles(null);
        String tmpResultString = tmpPSmiles.getErrorMessage();
        String tmpExpectString = MessagePSmiles.getString("StructureCheck.NoTokens");
        assertTrue("Test3.1", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.NoTokens");
        assertTrue("Test3.2", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A*-A");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidCharacter");
        assertTrue("Test3.3", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("0A");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidFirstCharacterOfStructure");
        assertTrue("Test3.4", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("{0A-A[Head]-B[Tail]}");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidFirstCharacterOfMonomer");
        assertTrue("Test3.5", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-2{(A)-B[HEAD]-B[TAIL]}");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidParticleAfterNormalClosingBracket");
        assertTrue("Test3.6", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("-B-B(A)-B");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidFirstCharacterOfStructure");
        assertTrue("Test3.7", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("B-B-B(A)-B[");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidLastCharacterOfStructure");
        assertTrue("Test3.8", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-2{B[HEAD]-B[TAIL]-}");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidLastCharacterOfMonomer");
        assertTrue("Test3.9", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("ASDFGJOCKELMENOCKEL");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidParticlename");
        assertTrue("Test3.10", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("123");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidParticlename");
        assertTrue("Test3.11", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("D");
        tmpResultString = tmpPSmiles.getErrorMessage();
        assertNull("Test3.12", tmpResultString);

        tmpPSmiles = new PSmiles("A-B-C");
        tmpResultString = tmpPSmiles.getErrorMessage();
        assertNull("Test3.13", tmpResultString);

        tmpPSmiles = new PSmiles("H2O");
        tmpResultString = tmpPSmiles.getErrorMessage();
        assertNull("Test3.13.1", tmpResultString);

        tmpPSmiles = new PSmiles("C6H50H");
        tmpResultString = tmpPSmiles.getErrorMessage();
        assertNull("Test3.13.2", tmpResultString);

        tmpPSmiles = new PSmiles("H2O-C6H5OH");
        tmpResultString = tmpPSmiles.getErrorMessage();
        assertNull("Test3.13.3", tmpResultString);

        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Checks curly brackets">
        tmpPSmiles = new PSmiles("A-{BC");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingClosingCurlyBracket");
        assertTrue("Test3.14", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-}BC");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingOpeningCurlyBracket");
        assertTrue("Test3.15", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A{{A-BC");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingClosingCurlyBracket");
        assertTrue("Test3.16", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A{A}-BC}");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingOpeningCurlyBracket");
        assertTrue("Test3.17", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-{}-BC");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.EmptyCurlyBrackets");
        assertTrue("Test3.18", tmpResultString.equals(tmpExpectString));

        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Checks angular brackets">
        tmpPSmiles = new PSmiles("A-B]B{A[HEAD]-B[TAIL]}-B");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingOpeningAngularBracket");
        assertTrue("Test3.19", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-B[B{A[HEAD]-B[TAIL]}(A)-B");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingClosingAngularBracket");
        assertTrue("Test3.20", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-B[HEAD]](B)-A");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingOpeningAngularBracket");
        assertTrue("Test3.21", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-B]](B)-D");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingOpeningAngularBracket");
        assertTrue("Test3.22", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-B[[1](B)-A");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingClosingAngularBracket");
        assertTrue("Test3.23", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A[A-B[1](B)-A");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingClosingAngularBracket");
        assertTrue("Test3.24", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A[A-B(A)-B");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingClosingAngularBracket");
        assertTrue("Test3.25", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-B[A](B)-A");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidCharacterBetweenAngularBrackets");
        assertTrue("Test3.26", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-B[3A](B)-A");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidCharacterBetweenAngularBrackets");
        assertTrue("Test3.27", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-B[HEAD]");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.IllegalUsingOfHeadOrTail");
        assertTrue("Test3.28", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-B[TAIL]");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.IllegalUsingOfHeadOrTail");
        assertTrue("Test3.29", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-B{A-[2A]}");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidCharacterBetweenAngularBrackets");
        assertTrue("Test3.30", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-B{A[TAIL]-B}-A");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingHeadAttribute");
        assertTrue("Test3.31", tmpResultString.equals(tmpExpectString));

        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Checks legal number of ring closures">
        tmpPSmiles = new PSmiles("A-B-A-B[1]{A[HEAD]-B[TAIL]}-A");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingRingClosure");
        assertTrue("Test3.32", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A[1]-B-A-B[1]{A[HEAD]-B[TAIL]}-A[1]");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.TooManyRingClosures");
        assertTrue("Test3.33", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-A[1]-A-A(B-A[2]-B)-B-A[1]-B-B");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingRingClosure");
        assertTrue("Test3.34", tmpResultString.equals(tmpExpectString));
        
        tmpPSmiles = new PSmiles("A[1]-B[1]");
        tmpResultString = tmpPSmiles.getErrorMessage();
        assertNull("Test3.35", tmpResultString);
        
        tmpPSmiles = new PSmiles("A[1][2]-B-C-D[1][2]");
        tmpResultString = tmpPSmiles.getErrorMessage();
        assertNull("Test3.36", tmpResultString);
        
        tmpPSmiles = new PSmiles("Et[1](Et(Pr-Pr))-Me[2](Et[2]-Et[3][4][6](CisButene[4][5]-MeOH-Et-Me[5][6](Me)))(Me)-Et[1][3]");
        tmpResultString = tmpPSmiles.getErrorMessage();
        assertNull("Test3.37", tmpResultString);

        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Checks normal brackets">
        tmpPSmiles = new PSmiles("A(A)-B(B{A[HEAD]-B[TAIL]}-B");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingClosingNormalBracket");
        assertTrue("Test3.35", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-B{A[HEAD]-A(B)-B[TAIL])-A}-A");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingOpeningNormalBracket");
        assertTrue("Test3.36", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A(B)-B)-B{A[HEAD]-A(B)-A[TAIL]}-B");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingOpeningNormalBracket");
        assertTrue("Test3.37", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A(A-B-A(B)A");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingClosingNormalBracket");
        assertTrue("Test3.38", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A)A-B-B(A)B");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingOpeningNormalBracket");
        assertTrue("Test3.39", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A)B-B-A(B");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingOpeningNormalBracket");
        assertTrue("Test3.40", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("ACA(4)");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingAParticlePriorNormalClosingBracket");
        assertTrue("Test3.40a", tmpResultString.equals(tmpExpectString));

        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Checks tokens">
        tmpPSmiles = new PSmiles("A-B((B))-B");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InSeriesOfNormalOpeningBrackets");
        assertTrue("Test3.41", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-B([1]-B[1])-A");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingAParticlePriorRingClosure");
        assertTrue("Test3.42", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-B(-B)-A");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingAParticlePriorConnection");
        assertTrue("Test3.43", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-B{A([TAIL]-B[HEAD])A}-B");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingAParticlePriorHeadOrTail");
        assertTrue("Test3.44", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A[1]-B(A-B)[1]-A");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingAParticlePriorRingClosure");
        assertTrue("Test3.45", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-{B[HEAD]-A-B(A)[TAIL]}-A");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingAParticlePriorHeadOrTail");
        assertTrue("Test3.46", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-B(A-B)A-B");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingAConnectionAfterNormalClosingBracket");
        assertTrue("Test3.47", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-B()B-A");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.EmptyNormalBrackets");
        assertTrue("Test3.48", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-B{A[HEAD]-B[TAIL]}-A-B");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingAConnectionPriorCurlyOpeningBracket");
        assertTrue("Test3.49", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-B-{A[HEAD]-B[TAIL]}[1]-A-B[1]");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingAParticlePriorRingClosure");
        assertTrue("Test3.50", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-(B)A");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingParticleBetweenConnectionAndNormalOpeningBracket");
        assertTrue("Test3.51", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-A(B-)A");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingParticleBetweenConnectionAndNormalClosingBracket");
        assertTrue("Test3.52", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-[1]-B-A[1]");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingParticleBetweenConnectionAndRingClosure");
        assertTrue("Test3.53", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-{A[HEAD]-B[TAIL]-}-A");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidLastCharacterOfMonomer");
        assertTrue("Test3.54", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A--B");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingParticleBetweenTwoConnections");
        assertTrue("Test3.55", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-{A[HEAD]-[TAIL]}-B");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingParticleBetweenConnectionAndHeadOrTail");
        assertTrue("Test3.56", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-{A[HEAD]-B[TAIL]#Hugo}-A");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MonomerAfterHeadOrTail");
        assertTrue("Test3.57", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-{A[HEAD]34-B[TAIL]}-B");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingAConnectionAfterHeadOrTail");
        assertTrue("Test3.58", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-#B#D-A");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MonomerAfterMonomer");
        assertTrue("Test3.59", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-{A[HEAD]-#Hugo-B[TAIL]}-A");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MonomerInMonomer");
        assertTrue("Test3.60", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-{A[HEAD]-B[TAIL]-#Hugo}-A");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MonomerInMonomer");
        assertTrue("Test3.61", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-{A[HEAD]-B[TAIL]-123}-A");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingParticleAfterNumber");
        assertTrue("Test3.62", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A[1]-123[1]-B");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingParticleAfterNumber");
        assertTrue("Test3.63", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-{A[HEAD]-123[TAIL]}-");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingParticleAfterNumber");
        assertTrue("Test3.64", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-#Hugo[HEAD]-B");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.IllegalUsingOfHeadOrTail");
        assertTrue("Test3.65", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-BBBB-A");
        tmpResultString = tmpPSmiles.getErrorMessage();
        assertNull("Test3.66", tmpResultString);

        tmpPSmiles = new PSmiles("A-AAAA-B");
        tmpResultString = tmpPSmiles.getErrorMessage();
        assertNull("Test3.67", tmpResultString);

        tmpPSmiles = new PSmiles("A-B-{123-A[HEAD]-B[TAIL]}-A-B");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingParticlePriorConnection");
        assertTrue("Test3.68", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("{A[HEAD]B-C[TAIL]}");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingAConnectionAfterHeadOrTail");
        assertTrue("Test3.69", tmpResultString.equals(tmpExpectString));

        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Checks monomer">
        tmpPSmiles = new PSmiles("A-{B[HEAD]-A[TAIL]-B[HEAD]}");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.TooManyHead");
        assertTrue("Test3.69", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-B-{2A[HEAD]-B[TAIL]}-B");
        tmpResultString = tmpPSmiles.getErrorMessage();
        assertNull("Test3.70", tmpResultString);

        tmpPSmiles = new PSmiles("A-{B[HEAD]-B[TAIL]-A[TAIL]}");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.TooManyTail");
        assertTrue("Test3.71", tmpResultString.equals(tmpExpectString));

        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Positive examples">
        tmpPSmiles = new PSmiles("A-23{A[HEAD]-B[TAIL]}-A[1]B-A-A-B[1]");
        tmpResultString = tmpPSmiles.getErrorMessage();
        assertNull("Test3.72", tmpResultString);

        tmpPSmiles = new PSmiles("A-B(B-A(B[1])-A)-A-A-B[1]-B");
        tmpResultString = tmpPSmiles.getErrorMessage();
        assertNull("Test3.73", tmpResultString);

        tmpPSmiles = new PSmiles("3A[1]-B-B-A[1]");
        tmpResultString = tmpPSmiles.getErrorMessage();
        assertNull("Test3.74", tmpResultString);

        tmpPSmiles = new PSmiles("A-B[1][2]-4B-A[1]-4A-B[2]");
        tmpResultString = tmpPSmiles.getErrorMessage();
        assertNull("Test3.75", tmpResultString);

        tmpPSmiles = new PSmiles("{B[HEAD]-A[TAIL]}");
        tmpResultString = tmpPSmiles.getErrorMessage();
        assertNull("Test3.76", tmpResultString);

        tmpPSmiles = new PSmiles("A-#MyMonomer");
        tmpResultString = tmpPSmiles.getErrorMessage();
        assertNull("Test3.77", tmpResultString);

        // excessive but allowed
        tmpPSmiles = new PSmiles("A-B(A)-B(A)");
        tmpResultString = tmpPSmiles.getErrorMessage();
        assertNull("Test3.78", tmpResultString);

        tmpPSmiles = new PSmiles("#MyMonomer");
        tmpResultString = tmpPSmiles.getErrorMessage();
        assertNull("Test3.79", tmpResultString);

        tmpPSmiles = new PSmiles("A-2(B-A-B)-B-A-B"); // illegal input
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidParticlePriorNormalBracket");
        assertEquals("Test3.80", tmpExpectString, tmpResultString);

        tmpPSmiles = new PSmiles("A-3(B-2(A-2(B)-2A))"); // illegal input
        tmpResultString = tmpPSmiles.getErrorMessage();
        assertEquals("Test3.81", tmpExpectString, tmpResultString);

        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Checks structure specified as monomer">
        tmpPSmiles = new PSmiles("A-{B[HEAD]-B[TAIL]}", true);
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.NoMonomer");
        assertTrue("Test3.82", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("{B-A[TAIL]}", true);
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingHeadAttribute");
        assertTrue("Test3.83", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-B-B", true);
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.NoMonomer");
        assertTrue("Test3.84", tmpResultString.equals(tmpExpectString));

        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Checks structure against available structures">
        String tmpStructure;
        HashMap<String, String> tmpAvailableParticles = new HashMap<>(10);
        tmpAvailableParticles.put("A", "A");
        tmpAvailableParticles.put("B", "B");

        tmpStructure = "A-B";
        tmpPSmiles = new PSmiles(tmpStructure, tmpAvailableParticles);
        assertNull("Test3.90", tmpPSmiles.getErrorMessage());

        tmpStructure = "123";
        tmpPSmiles = new PSmiles(tmpStructure, tmpAvailableParticles);
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidParticlename");
        assertTrue("Test3.91", tmpPSmiles.getErrorMessage().equals(tmpExpectString));

        tmpStructure = "D";
        tmpPSmiles = new PSmiles(tmpStructure, tmpAvailableParticles);
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidParticlename");
        assertTrue("Test3.92", tmpPSmiles.getErrorMessage().equals(tmpExpectString));

        tmpStructure = "A-D";
        tmpPSmiles = new PSmiles(tmpStructure, tmpAvailableParticles);
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidParticlename");
        assertTrue("Test3.93", tmpPSmiles.getErrorMessage().equals(tmpExpectString));

        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Checks structure with [START]- and [END]-tags ">
        tmpPSmiles = new PSmiles("A[START]-B-C[END]");
        assertNull("Test3.100", tmpPSmiles.getErrorMessage());

        tmpPSmiles = new PSmiles("3A[START]-B-C[END]");
        assertNull("Test3.101", tmpPSmiles.getErrorMessage());

        tmpPSmiles = new PSmiles("A[START]-B[START]-C[END]");
        tmpExpectString = MessagePSmiles.getString("StructureCheck.TooManyStartTag");
        assertTrue("Test3.102", tmpPSmiles.getErrorMessage().equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A[END]-B[START]-C[END]");
        tmpExpectString = MessagePSmiles.getString("StructureCheck.TooManyEndTag");
        assertTrue("Test3.103", tmpPSmiles.getErrorMessage().equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-B[START]");
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingEndAttribute");
        assertTrue("Test3.104", tmpPSmiles.getErrorMessage().equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-B[END]");
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingStartAttribute");
        assertTrue("Test3.105", tmpPSmiles.getErrorMessage().equals(tmpExpectString));

        tmpPSmiles = new PSmiles("{A-B[START]}");
        tmpExpectString = MessagePSmiles.getString("StructureCheck.StartAttributeInMonomer");
        assertTrue("Test3.106", tmpPSmiles.getErrorMessage().equals(tmpExpectString));

        tmpPSmiles = new PSmiles("{A-B[END]}");
        tmpExpectString = MessagePSmiles.getString("StructureCheck.EndAttributeInMonomer");
        assertTrue("Test3.107", tmpPSmiles.getErrorMessage().equals(tmpExpectString));

        tmpPSmiles = new PSmiles("[START]A-B[END]");
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidFirstCharacterOfStructure");
        assertTrue("Test3.108", tmpPSmiles.getErrorMessage().equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-[START]B-C[END]");
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidParticleAfterConnection");
        assertTrue("Test3.109", tmpPSmiles.getErrorMessage().equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-B[START]-[END]C");
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidParticleAfterConnection");
        assertTrue("Test3.109", tmpPSmiles.getErrorMessage().equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A[START][1]-B-C[END][1]");
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidParticleAfterConnection");
        assertNull("Test3.110", tmpPSmiles.getErrorMessage());

        tmpPSmiles = new PSmiles("A[1][START]-B-C[END][1]");
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidParticleAfterConnection");
        assertNull("Test3.111", tmpPSmiles.getErrorMessage());

        tmpPSmiles = new PSmiles("A[1][START]5-B-C[END][1]");
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidParticleAfterStartEnd");
        assertTrue("Test3.112", tmpPSmiles.getErrorMessage().equals(tmpExpectString));
        
        tmpPSmiles = new PSmiles("<A'1'[START](B4-C[1])(C[END]-A'2')-D[1]-3E-4{A[HEAD]-B[TAIL]}><H2O-#Hugo>");
        assertNull("Test3.113", tmpPSmiles.getErrorMessage());
        
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Checks structure with < and >-tags ">
        tmpPSmiles = new PSmiles("<BC");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingClosingAngleBracket");
        assertTrue("Test3.113", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A-BC>");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingOpeningAngleBracket");
        assertTrue("Test3.114", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("<A-BC<");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingClosingAngleBracket");
        assertTrue("Test3.115", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("<AA>BC>}");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingOpeningAngleBracket");
        assertTrue("Test3.116", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("<>");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.EmptyAngleBrackets");
        assertTrue("Test3.117", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("<><A-B>");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.EmptyAngleBrackets");
        assertTrue("Test3.118", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("<A-B><>");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.EmptyAngleBrackets");
        assertTrue("Test3.119", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("<(A-B)>");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingConnection");
        assertTrue("Test3.120", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("<-A-B>");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidFirstCharacterOfStructure");
        assertTrue("Test3.121", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("<A-B>A");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidParticleAfterAngleClosingBracket");
        assertTrue("Test3.122", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("<A>B<C>");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidCharacterPriorAngularBracket");
        assertTrue("Test3.123", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("A<B><C>");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidCharacterPriorAngularBracket");
        assertTrue("Test3.124", tmpResultString.equals(tmpExpectString));

        tmpPSmiles = new PSmiles("<A><B>C");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidParticleAfterAngleClosingBracket");
        assertTrue("Test3.125", tmpResultString.equals(tmpExpectString));

        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Checks structure with backbone tags">
        tmpPSmiles = new PSmiles("A'");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingBackboneIndex");
        assertTrue("Test3.126", tmpResultString.equals(tmpExpectString));
        // </editor-fold>
    }

    /**
     * Test property of molecular structure related methods
     */
    public void testGetStructureTokens() {
        String[] tmpTokens;

        PSmiles tmpPSmiles = new PSmiles("A-B-C");
        tmpTokens = tmpPSmiles.getStructureTokens();
        assertTrue("Test4.1.1", tmpTokens.length == 8);
        assertTrue("Test4.1.2", tmpTokens[0].equals("1"));
        assertTrue("Test4.1.3", tmpTokens[1].equals("A"));
        assertTrue("Test4.1.4", tmpTokens[2].equals("-"));
        assertTrue("Test4.1.5", tmpTokens[3].equals("1"));
        assertTrue("Test4.1.6", tmpTokens[4].equals("B"));
        assertTrue("Test4.1.7", tmpTokens[5].equals("-"));
        assertTrue("Test4.1.8", tmpTokens[6].equals("1"));
        assertTrue("Test4.1.9", tmpTokens[7].equals("C"));

        tmpPSmiles = new PSmiles("3A(B)-D");
        tmpTokens = tmpPSmiles.getStructureTokens();
        assertTrue("Test4.2", tmpTokens.length == 15);

        tmpPSmiles = new PSmiles("3ALA(B)-D");
        tmpTokens = tmpPSmiles.getStructureTokens();
        assertTrue("Test4.3", tmpTokens.length == 15);

        tmpPSmiles = new PSmiles("A-B(D-E(G-H[1])-F)-I-A-K[1]-B");
        tmpTokens = tmpPSmiles.getStructureTokens();
        assertTrue("Test4.4", tmpTokens.length == 36);

        tmpPSmiles = new PSmiles(" A - B(D-E(G-H[1])-F) - I - A - K[1] - B");
        tmpTokens = tmpPSmiles.getStructureTokens();
        assertTrue("Test4.5", tmpTokens.length == 36);

        tmpPSmiles = new PSmiles("A[1]-B-C-D[1]");
        tmpTokens = tmpPSmiles.getStructureTokens();
        assertTrue("Test4.6", tmpTokens.length == 13);

        tmpPSmiles = new PSmiles("A-B(C)(D)(E)(F)-G");
        tmpTokens = tmpPSmiles.getStructureTokens();
        assertTrue("Test4.7", tmpTokens.length == 24);

        // Example for a disconnected structure
        tmpPSmiles = new PSmiles("(A)3(B(C)-D)");
        tmpTokens = tmpPSmiles.getStructureTokens();
        assertTrue("Test4.8.1", tmpTokens.length == 16);
        assertEquals("Test4.8.2a", tmpTokens[0], "(");
        assertEquals("Test4.8.2b", tmpTokens[1], "1");
    }

    /**
     * Test of property getParticleIndex
     */
    public void testGetParticleIndices() {
        PSmiles tmpPSmiles;
        int[] tmpResultParticleIndex;
        String tmpResultString;

        tmpPSmiles = new PSmiles(null);
        tmpResultParticleIndex = tmpPSmiles.getParticleIndices();
        assertNull("Test5.1", tmpResultParticleIndex);

        tmpPSmiles = new PSmiles("");
        tmpResultParticleIndex = tmpPSmiles.getParticleIndices();
        assertNull("Test5.2", tmpResultParticleIndex);

        tmpPSmiles = new PSmiles("A-B-C(D)-E-F");
        // The structure token in this case is "1A-1B-1C(1D)-1E-1F" - thereby the index of first particle is 1 not 0!
        tmpResultParticleIndex = tmpPSmiles.getParticleIndices();
        assertTrue("Test5.3", tmpResultParticleIndex.length == 6
                && tmpResultParticleIndex[0] == 1
                && tmpResultParticleIndex[1] == 4
                && tmpResultParticleIndex[2] == 7
                && tmpResultParticleIndex[3] == 10
                && tmpResultParticleIndex[4] == 14
                && tmpResultParticleIndex[5] == 17);

        tmpPSmiles = new PSmiles("3A-B(2C)-3D");
        tmpResultParticleIndex = tmpPSmiles.getParticleIndices();
        assertTrue("Test5.4", tmpResultParticleIndex.length == 9
                && tmpResultParticleIndex[0] == 1
                && tmpResultParticleIndex[1] == 4
                && tmpResultParticleIndex[2] == 7
                && tmpResultParticleIndex[3] == 10
                && tmpResultParticleIndex[4] == 13
                && tmpResultParticleIndex[5] == 16
                && tmpResultParticleIndex[6] == 20
                && tmpResultParticleIndex[7] == 23
                && tmpResultParticleIndex[8] == 26);

        tmpPSmiles = new PSmiles("2{3A[HEAD]-B-C[TAIL]}");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpResultParticleIndex = tmpPSmiles.getParticleIndices();
        assertNull("Test5.6.1", tmpResultString);
        assertTrue("Test5.6.2", tmpResultParticleIndex.length == 10
                && tmpResultParticleIndex[0] == 2
                && tmpResultParticleIndex[1] == 5
                && tmpResultParticleIndex[2] == 8
                && tmpResultParticleIndex[3] == 12
                && tmpResultParticleIndex[4] == 15
                && tmpResultParticleIndex[5] == 20
                && tmpResultParticleIndex[6] == 23
                && tmpResultParticleIndex[7] == 26
                && tmpResultParticleIndex[8] == 30
                && tmpResultParticleIndex[9] == 33);

        tmpPSmiles = new PSmiles("A");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpResultParticleIndex = tmpPSmiles.getParticleIndices();
        assertNull("Test5.7.1", tmpResultString);
        assertTrue("Test5.7.2", tmpResultParticleIndex.length == 1
                && tmpResultParticleIndex[0] == 1);

        // Example for a disconnected structure
        tmpPSmiles = new PSmiles("(A)3(B(C)-D)");
        tmpResultParticleIndex = tmpPSmiles.getParticleIndices();
        assertNull("Test5.7.3", tmpResultParticleIndex); // If there is a disconnected structure, use getParticleIndices at disconnected substructure. 
    }

    /**
     * Test of property getMonomers
     */
    public void testGetMonomers() {

        PSmiles tmpPSmiles;
        String[] tmpResultParticleName;

        tmpPSmiles = new PSmiles(null);
        tmpResultParticleName = tmpPSmiles.getMonomers();
        assertNull("Test7.1", tmpResultParticleName);

        tmpPSmiles = new PSmiles("");
        tmpResultParticleName = tmpPSmiles.getMonomers();
        assertNull("Test7.2", tmpResultParticleName);

        tmpPSmiles = new PSmiles("#A-B-#C");
        tmpResultParticleName = tmpPSmiles.getMonomers();
        assertTrue("Test7.3.1", tmpResultParticleName.length == 2);
        assertTrue("Test7.3.2", Arrays.asList(tmpResultParticleName).contains("#A"));
        assertTrue("Test7.3.4", Arrays.asList(tmpResultParticleName).contains("#C"));
    }

    /**
     * Test of property getPartOfPSmiles
     */
    public void testGetPartOfPSmiles() {
        PSmiles tmpPSmiles;
        PSmilesInner[] tmpPSmilesArray;

        tmpPSmiles = new PSmiles("<A>");
        assertFalse("Test7a.1.1", tmpPSmiles.hasMultipleParts());
        assertEquals("Test7a.1.2", tmpPSmiles.getErrorMessage(), null);

        tmpPSmiles = new PSmiles("A");
        assertFalse("Test7a.1.4", tmpPSmiles.hasMultipleParts());

        tmpPSmiles = new PSmiles("<A-A><B>");
        assertTrue("Test7a.2.1", tmpPSmiles.hasMultipleParts());
        assertEquals("Test7a.2.2", tmpPSmiles.getPartsOfPSmiles().length, 2);
        assertEquals("Test7a.2.3", tmpPSmiles.getPartsOfPSmiles()[0].getInnerParticles()[0], "A");
        assertEquals("Test7a.2.4", tmpPSmiles.getPartsOfPSmiles()[0].getInnerParticles()[1], "A");
        assertEquals("Test7a.2.5", tmpPSmiles.getPartsOfPSmiles()[1].getInnerParticles()[0], "B");
        assertEquals("Test7a.2.6", tmpPSmiles.getPartsOfPSmiles()[0].getInputStructure(), "A-A");
        assertEquals("Test7a.2.7", tmpPSmiles.getPartsOfPSmiles()[1].getInputStructure(), "B");

        HashMap<String, String> tmpAvailableParticles = new HashMap<>();
        tmpPSmiles.setInputStructure("<A-A><B>", tmpAvailableParticles, false);
        assertTrue("Test7a.3.1", tmpPSmiles.hasMultipleParts());
        tmpPSmilesArray = tmpPSmiles.getPartsOfPSmiles();
        assertNotNull("Test7a.3.2", tmpPSmilesArray);
        assertEquals("Test7a.3.3", tmpPSmilesArray.length, 2);
        assertEquals("Test7a.3.4", tmpPSmilesArray[0].getInnerParticles()[0], "A");
        assertEquals("Test7a.3.5", tmpPSmilesArray[0].getInnerParticles()[1], "A");
        assertEquals("Test7a.3.6", tmpPSmilesArray[1].getInnerParticles()[0], "B");
        assertEquals("Test7a.3.7", tmpPSmilesArray[0].getInputStructure(), "A-A");
        assertEquals("Test7a.3.8", tmpPSmilesArray[1].getInputStructure(), "B");

        tmpPSmiles = new PSmiles("<A>");
        assertFalse("Test7a.4.1", tmpPSmiles.hasMultipleParts());
        tmpPSmilesArray = tmpPSmiles.getPartsOfPSmiles();
        assertNotNull("Test7a.4.2", tmpPSmilesArray);
        assertEquals("Test7a.4.3", tmpPSmilesArray.length, 1);
        assertEquals("Test7a.4.4", tmpPSmilesArray[0].getInputStructure(), "A");

        tmpPSmiles = new PSmiles("A");
        assertFalse("Test7a.5.4", tmpPSmiles.hasMultipleParts());
        tmpPSmilesArray = tmpPSmiles.getPartsOfPSmiles();
        assertNotNull("Test7a.5.2", tmpPSmilesArray);
        assertEquals("Test7a.5.3", tmpPSmilesArray.length, 1);
        assertEquals("Test7a.5.4", tmpPSmilesArray[0].getInputStructure(), "A");

        tmpPSmiles = new PSmiles("3<A-B-C>");
        assertTrue("Test 7a.6.1", tmpPSmiles.hasMultipleParts());
        tmpPSmilesArray = tmpPSmiles.getPartsOfPSmiles();
        assertEquals("Test7a.6.2", tmpPSmilesArray.length, 3);
        assertEquals("Test7a.6.3", tmpPSmilesArray[0].getInputStructure(), "A-B-C");
        assertEquals("Test7a.6.4", tmpPSmilesArray[0].getInnerParticles()[0], "A");
        assertEquals("Test7a.6.5", tmpPSmilesArray[0].getInnerParticles()[1], "B");
        assertEquals("Test7a.6.6", tmpPSmilesArray[0].getInnerParticles()[2], "C");
        assertEquals("Test7a.6.7", tmpPSmilesArray[1].getInnerParticles()[0], "A");
        assertEquals("Test7a.6.8", tmpPSmilesArray[1].getInnerParticles()[1], "B");
        assertEquals("Test7a.6.9", tmpPSmilesArray[1].getInnerParticles()[2], "C");
        assertEquals("Test7a.6.10", tmpPSmilesArray[2].getInnerParticles()[0], "A");
        assertEquals("Test7a.6.11", tmpPSmilesArray[2].getInnerParticles()[1], "B");
        assertEquals("Test7a.6.12", tmpPSmilesArray[2].getInnerParticles()[2], "C");

        tmpPSmiles = new PSmiles("11<A-B>");
        assertEquals("Test7a.7.1", tmpPSmiles.getPartsOfPSmiles().length, 11);

        tmpPSmiles = new PSmiles("<A-B>2<C>3<D-E>");
        tmpPSmilesArray = tmpPSmiles.getPartsOfPSmiles();
        assertEquals("Test7a.8.1", tmpPSmilesArray.length, 6);
        assertEquals("Test7a.8.2", tmpPSmilesArray[0].getInnerParticles()[0], "A");
        assertEquals("Test7a.8.3", tmpPSmilesArray[0].getInnerParticles()[1], "B");
        assertEquals("Test7a.8.4", tmpPSmilesArray[1].getInnerParticles()[0], "C");
        assertEquals("Test7a.8.5", tmpPSmilesArray[2].getInnerParticles()[0], "C");
        assertEquals("Test7a.8.6", tmpPSmilesArray[3].getInnerParticles()[0], "D");
        assertEquals("Test7a.8.7", tmpPSmilesArray[3].getInnerParticles()[1], "E");
        assertEquals("Test7a.8.8", tmpPSmilesArray[4].getInnerParticles()[0], "D");
        assertEquals("Test7a.8.9", tmpPSmilesArray[4].getInnerParticles()[1], "E");
        assertEquals("Test7a.8.10", tmpPSmilesArray[5].getInnerParticles()[0], "D");
        assertEquals("Test7a.8.11", tmpPSmilesArray[5].getInnerParticles()[1], "E");
    }

    /**
     * Test of getParticleCoordinateAndConnectionInformation method
     */
    public void testGetPSmilesMatrix() {

        // <editor-fold defaultstate="collapsed" desc="local variables">
        String[][] tmpResult;

        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Simple tests">
        PSmiles tmpPSmiles = new PSmiles(null);
        tmpResult = tmpPSmiles.getParticlePositionsAndConnections();
        assertTrue("Test9.1", tmpResult == null);

        tmpPSmiles = new PSmiles("");
        tmpResult = tmpPSmiles.getParticlePositionsAndConnections();
        assertTrue("Test9.2", tmpResult == null);

        tmpPSmiles = new PSmiles("A");
        tmpResult = tmpPSmiles.getParticlePositionsAndConnections();
        assertTrue("Test9.2a", tmpResult[0][0].equals("1")
                && tmpResult[0][1].equals("A")
                && tmpResult[0][2].equals("0")
                && tmpResult[0][3].equals("")
                && tmpResult[0][4].equals("")
                && tmpResult[0][5].equals(""));

        tmpPSmiles = new PSmiles("A-B");
        tmpResult = tmpPSmiles.getParticlePositionsAndConnections();
        assertTrue("Test9.3", // New_Index
                tmpResult[0][0].equals("1")
                && tmpResult[0][1].equals("A")
                && tmpResult[0][2].equals("0")
                && tmpResult[0][3].equals("")
                && tmpResult[0][4].equals("")
                && tmpResult[0][5].equals("")
                && tmpResult[0][6].equals("1") // New_Index
                && tmpResult[1][0].equals("2")
                && tmpResult[1][1].equals("B")
                && tmpResult[1][2].equals("0")
                && tmpResult[1][3].equals("")
                && tmpResult[1][4].equals("")
                && tmpResult[1][5].equals("")
                && tmpResult[1][6].equals("-1")
                && tmpResult.length == 2);

        tmpPSmiles = new PSmiles("<A-B>");
        tmpResult = tmpPSmiles.getParticlePositionsAndConnections();
        assertTrue("Test9.3a", // New_Index
                tmpResult[0][0].equals("1")
                && tmpResult[0][1].equals("A")
                && tmpResult[0][2].equals("0")
                && tmpResult[0][3].equals("")
                && tmpResult[0][4].equals("")
                && tmpResult[0][5].equals("")
                && tmpResult[0][6].equals("1") // New_Index
                && tmpResult[1][0].equals("2")
                && tmpResult[1][1].equals("B")
                && tmpResult[1][2].equals("0")
                && tmpResult[1][3].equals("")
                && tmpResult[1][4].equals("")
                && tmpResult[1][5].equals("")
                && tmpResult[1][6].equals("-1")
                && tmpResult.length == 2);

        tmpPSmiles = new PSmiles("A-B(C-D-E)-F");
        tmpResult = tmpPSmiles.getParticlePositionsAndConnections();
        assertTrue("Test9.4", // New_Index
                tmpResult[0][0].equals("1")
                && tmpResult[0][1].equals("A")
                && tmpResult[0][2].equals("0")
                && tmpResult[0][3].equals("")
                && tmpResult[0][4].equals("")
                && tmpResult[0][5].equals("")
                && tmpResult[0][6].equals("1") // New_Index
                && tmpResult[1][0].equals("2")
                && tmpResult[1][1].equals("B")
                && tmpResult[1][2].equals("0")
                && tmpResult[1][3].equals("")
                && tmpResult[1][4].equals("")
                && tmpResult[1][5].equals("")
                && tmpResult[1][6].equals("-1")
                && tmpResult[1][7].equals("1")
                && tmpResult[1][8].equals("4") // New_Index
                && tmpResult[2][0].equals("3")
                && tmpResult[2][1].equals("C")
                && tmpResult[2][2].equals("0")
                && tmpResult[2][3].equals("")
                && tmpResult[2][4].equals("")
                && tmpResult[2][5].equals("")
                && tmpResult[2][6].equals("-1")
                && tmpResult[2][7].equals("1") // New_Index
                && tmpResult[3][0].equals("4")
                && tmpResult[3][1].equals("D")
                && tmpResult[3][2].equals("0")
                && tmpResult[3][3].equals("")
                && tmpResult[3][4].equals("")
                && tmpResult[3][5].equals("")
                && tmpResult[3][6].equals("-1")
                && tmpResult[3][7].equals("1") // New_Index
                && tmpResult[4][0].equals("5")
                && tmpResult[4][1].equals("E")
                && tmpResult[4][2].equals("0")
                && tmpResult[4][3].equals("")
                && tmpResult[4][4].equals("")
                && tmpResult[4][5].equals("")
                && tmpResult[4][6].equals("-1") // New_Index
                && tmpResult[5][0].equals("6")
                && tmpResult[5][1].equals("F")
                && tmpResult[5][2].equals("0")
                && tmpResult[5][3].equals("")
                && tmpResult[5][4].equals("")
                && tmpResult[5][5].equals("")
                && tmpResult[5][6].equals("-4")
                && tmpResult.length == 6);

        // With different start index
        tmpPSmiles = new PSmiles("A-B(C-D-E)-F", false, 9);
        tmpResult = tmpPSmiles.getParticlePositionsAndConnections();
        assertTrue("Test9.5", // New_Index
                tmpResult[0][0].equals("9")
                && tmpResult[0][1].equals("A")
                && tmpResult[0][2].equals("0")
                && tmpResult[0][3].equals("")
                && tmpResult[0][4].equals("")
                && tmpResult[0][5].equals("")
                && tmpResult[0][6].equals("1") // New_Index
                && tmpResult[1][0].equals("10")
                && tmpResult[1][1].equals("B")
                && tmpResult[1][2].equals("0")
                && tmpResult[1][3].equals("")
                && tmpResult[1][4].equals("")
                && tmpResult[1][5].equals("")
                && tmpResult[1][6].equals("-1")
                && tmpResult[1][7].equals("1")
                && tmpResult[1][8].equals("4") // New_Index
                && tmpResult[2][0].equals("11")
                && tmpResult[2][1].equals("C")
                && tmpResult[2][2].equals("0")
                && tmpResult[2][3].equals("")
                && tmpResult[2][4].equals("")
                && tmpResult[2][5].equals("")
                && tmpResult[2][6].equals("-1")
                && tmpResult[2][7].equals("1") // New_Index
                && tmpResult[3][0].equals("12")
                && tmpResult[3][1].equals("D")
                && tmpResult[3][2].equals("0")
                && tmpResult[3][3].equals("")
                && tmpResult[3][4].equals("")
                && tmpResult[3][5].equals("")
                && tmpResult[3][6].equals("-1")
                && tmpResult[3][7].equals("1") // New_Index
                && tmpResult[4][0].equals("13")
                && tmpResult[4][1].equals("E")
                && tmpResult[4][2].equals("0")
                && tmpResult[4][3].equals("")
                && tmpResult[4][4].equals("")
                && tmpResult[4][5].equals("")
                && tmpResult[4][6].equals("-1") // New_Index
                && tmpResult[5][0].equals("14")
                && tmpResult[5][1].equals("F")
                && tmpResult[5][2].equals("0")
                && tmpResult[5][3].equals("")
                && tmpResult[5][4].equals("")
                && tmpResult[5][5].equals("")
                && tmpResult[5][6].equals("-4")
                && tmpResult.length == 6);

        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Test overloaded methods">
        PointInSpace tmpFirstParticle = new PointInSpace(0.0, 0.0, 0.0);
        PointInSpace tmpLastParticle = new PointInSpace(10.0, 10.0, 10.0);
        double tmpBondLength = 1.0;
        tmpPSmiles = new PSmiles("A-B-C(D)-E-F", false, 1, tmpFirstParticle, tmpLastParticle, tmpBondLength);
        tmpResult = tmpPSmiles.getParticlePositionsAndConnections();
        assertTrue("Test9.6", // New_Index
                tmpResult[0][0].equals("1")
                && tmpResult[0][1].equals("A")
                && tmpResult[0][2].equals("0")
                && tmpResult[0][3].equals("0.0")
                && tmpResult[0][4].equals("0.0")
                && tmpResult[0][5].equals("0.0")
                && tmpResult[0][6].equals("1") // New_Index
                && tmpResult[1][0].equals("2")
                && tmpResult[1][1].equals("B")
                && tmpResult[1][2].equals("0")
                && tmpResult[1][3].equals("0.5773502691896257")
                && tmpResult[1][4].equals("0.5773502691896257")
                && tmpResult[1][5].equals("0.5773502691896257")
                && tmpResult[1][6].equals("-1")
                && tmpResult[1][7].equals("1") // New_Index
                && tmpResult[2][0].equals("3")
                && tmpResult[2][1].equals("C")
                && tmpResult[2][2].equals("0")
                && tmpResult[2][3].equals("1.1547005383792515")
                && tmpResult[2][4].equals("1.1547005383792515")
                && tmpResult[2][5].equals("1.1547005383792515")
                && tmpResult[2][6].equals("-1")
                && tmpResult[2][7].equals("1")
                && tmpResult[2][8].equals("2") // New_Index
                && tmpResult[3][0].equals("4")
                && tmpResult[3][1].equals("D")
                && tmpResult[3][2].equals("0")
                && tmpResult[3][3].equals("1.1547005383792515")
                && tmpResult[3][4].equals("1.1547005383792515")
                && tmpResult[3][5].equals("1.1547005383792515")
                && tmpResult[3][6].equals("-1") // New_Index
                && tmpResult[4][0].equals("5")
                && tmpResult[4][1].equals("E")
                && tmpResult[4][2].equals("0")
                && tmpResult[4][3].equals("1.7320508075688772")
                && tmpResult[4][4].equals("1.7320508075688772")
                && tmpResult[4][5].equals("1.7320508075688772")
                && tmpResult[4][6].equals("-2")
                && tmpResult[4][7].equals("1") // New_Index
                && tmpResult[5][0].equals("6")
                && tmpResult[5][1].equals("F")
                && tmpResult[5][2].equals("0")
                && tmpResult[5][3].equals("2.309401076758503")
                && tmpResult[5][4].equals("2.309401076758503")
                && tmpResult[5][5].equals("2.309401076758503")
                && tmpResult[5][6].equals("-1"));

        tmpFirstParticle = new PointInSpace(0.0, 0.0, 0.0);
        tmpLastParticle = new PointInSpace(1.0, 1.0, 1.0);
        tmpBondLength = 1.0;
        tmpPSmiles = new PSmiles("A-B-C(D)-E-F", false, 1, tmpFirstParticle, tmpLastParticle, tmpBondLength);
        tmpResult = tmpPSmiles.getParticlePositionsAndConnections();
        assertTrue("Test9.7", // New_Index
                tmpResult[0][0].equals("1")
                && tmpResult[0][1].equals("A")
                && tmpResult[0][2].equals("0")
                && tmpResult[0][3].equals("0.0")
                && tmpResult[0][4].equals("0.0")
                && tmpResult[0][5].equals("0.0")
                && tmpResult[0][6].equals("1") // New_Index
                && tmpResult[1][0].equals("2")
                && tmpResult[1][1].equals("B")
                && tmpResult[1][2].equals("0")
                && tmpResult[1][3].equals("0.25")
                && tmpResult[1][4].equals("0.25")
                && tmpResult[1][5].equals("0.25")
                && tmpResult[1][6].equals("-1")
                && tmpResult[1][7].equals("1") // New_Index
                && tmpResult[2][0].equals("3")
                && tmpResult[2][1].equals("C")
                && tmpResult[2][2].equals("0")
                && tmpResult[2][3].equals("0.5")
                && tmpResult[2][4].equals("0.5")
                && tmpResult[2][5].equals("0.5")
                && tmpResult[2][6].equals("-1")
                && tmpResult[2][7].equals("1")
                && tmpResult[2][8].equals("2") // New_Index
                && tmpResult[3][0].equals("4")
                && tmpResult[3][1].equals("D")
                && tmpResult[3][2].equals("0")
                && tmpResult[3][3].equals("0.5")
                && tmpResult[3][4].equals("0.5")
                && tmpResult[3][5].equals("0.5")
                && tmpResult[3][6].equals("-1") // New_Index
                && tmpResult[4][0].equals("5")
                && tmpResult[4][1].equals("E")
                && tmpResult[4][2].equals("0")
                && tmpResult[4][3].equals("0.75")
                && tmpResult[4][4].equals("0.75")
                && tmpResult[4][5].equals("0.75")
                && tmpResult[4][6].equals("-2")
                && tmpResult[4][7].equals("1") // New_Index
                && tmpResult[5][0].equals("6")
                && tmpResult[5][1].equals("F")
                && tmpResult[5][2].equals("0")
                && tmpResult[5][3].equals("1.0")
                && tmpResult[5][4].equals("1.0")
                && tmpResult[5][5].equals("1.0")
                && tmpResult[5][6].equals("-1"));

        tmpFirstParticle = new PointInSpace(0.0, 0.0, 0.0);
        tmpLastParticle = new PointInSpace(1.0, 1.0, 1.0);
        tmpBondLength = 1.0;
        tmpPSmiles = new PSmiles("3A-B(2C)-3D", false, 1, tmpFirstParticle, tmpLastParticle, tmpBondLength);
        tmpResult = tmpPSmiles.getParticlePositionsAndConnections();
        assertTrue("Test9.8", // New_Index
                tmpResult[0][0].equals("1")
                && tmpResult[0][1].equals("A")
                && tmpResult[0][2].equals("0")
                && tmpResult[0][3].equals("0.0")
                && tmpResult[0][4].equals("0.0")
                && tmpResult[0][5].equals("0.0")
                && tmpResult[0][6].equals("1") // New_Index
                && tmpResult[1][0].equals("2")
                && tmpResult[1][1].equals("A")
                && tmpResult[1][2].equals("0")
                && tmpResult[1][3].equals("0.16666666666666666")
                && tmpResult[1][4].equals("0.16666666666666666")
                && tmpResult[1][5].equals("0.16666666666666666")
                && tmpResult[1][6].equals("-1")
                && tmpResult[1][7].equals("1") // New_Index
                && tmpResult[2][0].equals("3")
                && tmpResult[2][1].equals("A")
                && tmpResult[2][2].equals("0")
                && tmpResult[2][3].equals("0.3333333333333333")
                && tmpResult[2][4].equals("0.3333333333333333")
                && tmpResult[2][5].equals("0.3333333333333333")
                && tmpResult[2][6].equals("-1")
                && tmpResult[2][7].equals("1") // New_Index
                && tmpResult[3][0].equals("4")
                && tmpResult[3][1].equals("B")
                && tmpResult[3][2].equals("0")
                && tmpResult[3][3].equals("0.5")
                && tmpResult[3][4].equals("0.5")
                && tmpResult[3][5].equals("0.5")
                && tmpResult[3][6].equals("-1")
                && tmpResult[3][7].equals("1") // New_Index
                && tmpResult[4][0].equals("5")
                && tmpResult[4][1].equals("C")
                && tmpResult[4][2].equals("0")
                && tmpResult[4][3].equals("0.5")
                && tmpResult[4][4].equals("0.5")
                && tmpResult[4][5].equals("0.5")
                && tmpResult[4][6].equals("-1")
                && tmpResult[4][7].equals("1") // New_Index
                && tmpResult[5][0].equals("6")
                && tmpResult[5][1].equals("C")
                && tmpResult[5][2].equals("0")
                && tmpResult[5][3].equals("0.5")
                && tmpResult[5][4].equals("0.5")
                && tmpResult[5][5].equals("0.5")
                && tmpResult[5][6].equals("-1") // New_Index
                && tmpResult[6][0].equals("7")
                && tmpResult[6][1].equals("D")
                && tmpResult[6][2].equals("0")
                && tmpResult[6][3].equals("0.6666666666666666")
                && tmpResult[6][4].equals("0.6666666666666666")
                && tmpResult[6][5].equals("0.6666666666666666")
                && tmpResult[6][6].equals("-3")
                && tmpResult[6][7].equals("1") // New_Index
                && tmpResult[7][0].equals("8")
                && tmpResult[7][1].equals("D")
                && tmpResult[7][2].equals("0")
                && tmpResult[7][3].equals("0.8333333333333333")
                && tmpResult[7][4].equals("0.8333333333333333")
                && tmpResult[7][5].equals("0.8333333333333333")
                && tmpResult[7][6].equals("-1")
                && tmpResult[7][7].equals("1") // New_Index
                && tmpResult[8][0].equals("9")
                && tmpResult[8][1].equals("D")
                && tmpResult[8][2].equals("0")
                && tmpResult[8][3].equals("1.0")
                && tmpResult[8][4].equals("1.0")
                && tmpResult[8][5].equals("1.0")
                && tmpResult[8][6].equals("-1"));

        PointInSpace[] tmpFirstParticles = new PointInSpace[3];
        PointInSpace[] tmpLastParticles = new PointInSpace[3];
        tmpFirstParticles[0] = new PointInSpace(0.0, 0.0, 0.0);
        tmpLastParticles[0] = new PointInSpace(4.0, 4.0, 4.0);
        tmpFirstParticles[1] = new PointInSpace(4.0, 4.0, 4.0);
        tmpLastParticles[1] = new PointInSpace(0.0, 0.0, 0.0);
        tmpFirstParticles[2] = new PointInSpace(1.0, 2.0, 3.0);
        tmpLastParticles[2] = new PointInSpace(3.0, 2.0, 1.0);
        tmpBondLength = 2;
        tmpPSmiles = new PSmiles("A-3B-CD", false, 1, tmpFirstParticles, tmpLastParticles, tmpBondLength);
        tmpResult = tmpPSmiles.getParticlePositionsAndConnections();
        assertTrue("Test9.9.1", // New_Index
                tmpResult[0][0].equals("1")
                && tmpResult[0][1].equals("A")
                && tmpResult[0][2].equals("0")
                && tmpResult[0][3].equals("0.0")
                && tmpResult[0][4].equals("0.0")
                && tmpResult[0][5].equals("0.0")
                && tmpResult[0][6].equals("1") // New_Index
                && tmpResult[1][0].equals("2")
                && tmpResult[1][1].equals("B")
                && tmpResult[1][2].equals("0")
                && tmpResult[1][3].equals("1.0")
                && tmpResult[1][4].equals("1.0")
                && tmpResult[1][5].equals("1.0")
                && tmpResult[1][6].equals("-1")
                && tmpResult[1][7].equals("1") // New_Index
                && tmpResult[2][0].equals("3")
                && tmpResult[2][1].equals("B")
                && tmpResult[2][2].equals("0")
                && tmpResult[2][3].equals("2.0")
                && tmpResult[2][4].equals("2.0")
                && tmpResult[2][5].equals("2.0")
                && tmpResult[2][6].equals("-1")
                && tmpResult[2][7].equals("1") // New_Index
                && tmpResult[3][0].equals("4")
                && tmpResult[3][1].equals("B")
                && tmpResult[3][2].equals("0")
                && tmpResult[3][3].equals("3.0")
                && tmpResult[3][4].equals("3.0")
                && tmpResult[3][5].equals("3.0")
                && tmpResult[3][6].equals("-1")
                && tmpResult[3][7].equals("1") // New_Index
                && tmpResult[4][0].equals("5")
                && tmpResult[4][1].equals("CD")
                && tmpResult[4][2].equals("0")
                && tmpResult[4][3].equals("4.0")
                && tmpResult[4][4].equals("4.0")
                && tmpResult[4][5].equals("4.0")
                && tmpResult[4][6].equals("-1"));

        assertTrue("Test9.9.2", // New_Index
                tmpResult[5][0].equals("6")
                && tmpResult[5][1].equals("A")
                && tmpResult[5][2].equals("0")
                && tmpResult[5][3].equals("4.0")
                && tmpResult[5][4].equals("4.0")
                && tmpResult[5][5].equals("4.0")
                && tmpResult[5][6].equals("1") // New_Index
                && tmpResult[6][0].equals("7")
                && tmpResult[6][1].equals("B")
                && tmpResult[6][2].equals("0")
                && tmpResult[6][3].equals("3.0")
                && tmpResult[6][4].equals("3.0")
                && tmpResult[6][5].equals("3.0")
                && tmpResult[6][6].equals("-1")
                && tmpResult[6][7].equals("1") // New_Index
                && tmpResult[7][0].equals("8")
                && tmpResult[7][1].equals("B")
                && tmpResult[7][2].equals("0")
                && tmpResult[7][3].equals("2.0")
                && tmpResult[7][4].equals("2.0")
                && tmpResult[7][5].equals("2.0")
                && tmpResult[7][6].equals("-1")
                && tmpResult[7][7].equals("1") // New_Index
                && tmpResult[8][0].equals("9")
                && tmpResult[8][1].equals("B")
                && tmpResult[8][2].equals("0")
                && tmpResult[8][3].equals("1.0")
                && tmpResult[8][4].equals("1.0")
                && tmpResult[8][5].equals("1.0")
                && tmpResult[8][6].equals("-1")
                && tmpResult[8][7].equals("1") // New_Index
                && tmpResult[9][0].equals("10")
                && tmpResult[9][1].equals("CD")
                && tmpResult[9][2].equals("0")
                && tmpResult[9][3].equals("0.0")
                && tmpResult[9][4].equals("0.0")
                && tmpResult[9][5].equals("0.0")
                && tmpResult[9][6].equals("-1"));

        assertTrue("Test9.9.3", // New_Index
                tmpResult[10][0].equals("11")
                && tmpResult[10][1].equals("A")
                && tmpResult[10][2].equals("0")
                && tmpResult[10][3].equals("1.0")
                && tmpResult[10][4].equals("2.0")
                && tmpResult[10][5].equals("3.0")
                && tmpResult[10][6].equals("1") // New_Index
                && tmpResult[11][0].equals("12")
                && tmpResult[11][1].equals("B")
                && tmpResult[11][2].equals("0")
                && tmpResult[11][3].equals("1.5")
                && tmpResult[11][4].equals("2.0")
                && tmpResult[11][5].equals("2.5")
                && tmpResult[11][6].equals("-1")
                && tmpResult[11][7].equals("1") // New_Index
                && tmpResult[12][0].equals("13")
                && tmpResult[12][1].equals("B")
                && tmpResult[12][2].equals("0")
                && tmpResult[12][3].equals("2.0")
                && tmpResult[12][4].equals("2.0")
                && tmpResult[12][5].equals("2.0")
                && tmpResult[12][6].equals("-1")
                && tmpResult[12][7].equals("1") // New_Index
                && tmpResult[13][0].equals("14")
                && tmpResult[13][1].equals("B")
                && tmpResult[13][2].equals("0")
                && tmpResult[13][3].equals("2.5")
                && tmpResult[13][4].equals("2.0")
                && tmpResult[13][5].equals("1.5")
                && tmpResult[13][6].equals("-1")
                && tmpResult[13][7].equals("1") // New_Index
                && tmpResult[14][0].equals("15")
                && tmpResult[14][1].equals("CD")
                && tmpResult[14][2].equals("0")
                && tmpResult[14][3].equals("3.0")
                && tmpResult[14][4].equals("2.0")
                && tmpResult[14][5].equals("1.0")
                && tmpResult[14][6].equals("-1"));

        tmpFirstParticle = new PointInSpace(0.0, 0.0, 0.0);
        tmpLastParticle = new PointInSpace(1.0, 1.0, 1.0);
        tmpBondLength = 1.0;
        tmpPSmiles = new PSmiles("A-B'1'-C-D-E'2'(F)-G-H'3'-I", false, 1, tmpFirstParticle, tmpLastParticle, tmpBondLength);
        tmpResult = tmpPSmiles.getParticlePositionsAndConnections();
        assertTrue("Test9.9.1", // New_Index
                tmpResult[0][0].equals("1")
                && tmpResult[0][1].equals("A")
                && tmpResult[0][2].equals("0")
                && tmpResult[1][0].equals("2")
                && tmpResult[1][1].equals("B")
                && tmpResult[1][2].equals("1")
                && tmpResult[2][0].equals("3")
                && tmpResult[2][1].equals("C")
                && tmpResult[2][2].equals("0")
                && tmpResult[3][0].equals("4")
                && tmpResult[3][1].equals("D")
                && tmpResult[3][2].equals("0")
                && tmpResult[4][0].equals("5")
                && tmpResult[4][1].equals("E")
                && tmpResult[4][2].equals("2")
                && tmpResult[5][0].equals("6")
                && tmpResult[5][1].equals("F")
                && tmpResult[5][2].equals("0")
                && tmpResult[6][0].equals("7")
                && tmpResult[6][1].equals("G")
                && tmpResult[6][2].equals("0")
                && tmpResult[7][0].equals("8")
                && tmpResult[7][1].equals("H")
                && tmpResult[7][2].equals("3")
                && tmpResult[8][0].equals("9")
                && tmpResult[8][1].equals("I")
                && tmpResult[8][2].equals("0"));

        tmpFirstParticles = new PointInSpace[2];
        tmpLastParticles = new PointInSpace[2];
        tmpFirstParticles[0] = new PointInSpace(1.0, 1.0, 1.0);
        tmpLastParticles[0] = new PointInSpace(2.0, 2.0, 2.0);
        tmpFirstParticles[1] = new PointInSpace(3.0, 3.0, 3.0);
        tmpLastParticles[1] = new PointInSpace(4.0, 4.0, 4.0);
        tmpBondLength = 1;
        tmpPSmiles = new PSmiles("<A><B>", 1, tmpFirstParticles, tmpLastParticles, tmpBondLength);
        tmpResult = tmpPSmiles.getParticlePositionsAndConnections();
        assertTrue("Test9.9.3a", tmpResult[0][0].equals("1")
                && tmpResult[0][1].equals("A")
                && tmpResult[0][2].equals("0")
                && tmpResult[0][3].equals("1.0")
                && tmpResult[0][4].equals("1.0")
                && tmpResult[0][5].equals("1.0")
                && tmpResult[1][0].equals("2")
                && tmpResult[1][1].equals("B")
                && tmpResult[1][2].equals("0")
                && tmpResult[1][3].equals("1.0")
                && tmpResult[1][4].equals("1.0")
                && tmpResult[1][5].equals("1.0")
                && tmpResult[2][0].equals("3")
                && tmpResult[2][1].equals("A")
                && tmpResult[2][2].equals("0")
                && tmpResult[2][3].equals("3.0")
                && tmpResult[2][4].equals("3.0")
                && tmpResult[2][5].equals("3.0")
                && tmpResult[3][0].equals("4")
                && tmpResult[3][1].equals("B")
                && tmpResult[3][2].equals("0")
                && tmpResult[3][3].equals("3.0")
                && tmpResult[3][4].equals("3.0")
                && tmpResult[3][5].equals("3.0"));

        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Test overloaded methods with [START]/[END]-Tags">
        tmpFirstParticle = new PointInSpace(0.0, 0.0, 0.0);
        tmpLastParticle = new PointInSpace(10.0, 10.0, 10.0);
        tmpBondLength = 1.0;
        tmpPSmiles = new PSmiles("A[START]-B-C(D)-E-F[END]", false, 1, tmpFirstParticle, tmpLastParticle, tmpBondLength);
        tmpResult = tmpPSmiles.getParticlePositionsAndConnections();
        assertTrue("Test9.9.4", // New_Index
                tmpResult[0][0].equals("1")
                && tmpResult[0][1].equals("A")
                && tmpResult[0][2].equals("0")
                && tmpResult[0][3].equals("0.0")
                && tmpResult[0][4].equals("0.0")
                && tmpResult[0][5].equals("0.0")
                && tmpResult[0][6].equals("1") // New_Index
                && tmpResult[1][0].equals("2")
                && tmpResult[1][1].equals("B")
                && tmpResult[1][2].equals("0")
                && tmpResult[1][3].equals("0.5773502691896257")
                && tmpResult[1][4].equals("0.5773502691896257")
                && tmpResult[1][5].equals("0.5773502691896257")
                && tmpResult[1][6].equals("-1")
                && tmpResult[1][7].equals("1") // New_Index
                && tmpResult[2][0].equals("3")
                && tmpResult[2][1].equals("C")
                && tmpResult[2][2].equals("0")
                && tmpResult[2][3].equals("1.1547005383792515")
                && tmpResult[2][4].equals("1.1547005383792515")
                && tmpResult[2][5].equals("1.1547005383792515")
                && tmpResult[2][6].equals("-1")
                && tmpResult[2][7].equals("1")
                && tmpResult[2][8].equals("2") // New_Index
                && tmpResult[3][0].equals("4")
                && tmpResult[3][1].equals("D")
                && tmpResult[3][2].equals("0")
                && tmpResult[3][3].equals("1.1547005383792515")
                && tmpResult[3][4].equals("1.1547005383792515")
                && tmpResult[3][5].equals("1.1547005383792515")
                && tmpResult[3][6].equals("-1") // New_Index
                && tmpResult[4][0].equals("5")
                && tmpResult[4][1].equals("E")
                && tmpResult[4][2].equals("0")
                && tmpResult[4][3].equals("1.7320508075688772")
                && tmpResult[4][4].equals("1.7320508075688772")
                && tmpResult[4][5].equals("1.7320508075688772")
                && tmpResult[4][6].equals("-2")
                && tmpResult[4][7].equals("1") // New_Index
                && tmpResult[5][0].equals("6")
                && tmpResult[5][1].equals("F")
                && tmpResult[5][2].equals("0")
                && tmpResult[5][3].equals("2.309401076758503")
                && tmpResult[5][4].equals("2.309401076758503")
                && tmpResult[5][5].equals("2.309401076758503")
                && tmpResult[5][6].equals("-1"));

        tmpFirstParticle = new PointInSpace(0.0, 0.0, 0.0);
        tmpLastParticle = new PointInSpace(10.0, 10.0, 10.0);
        tmpBondLength = 1.0;
        tmpPSmiles = new PSmiles("A[END]-B-C(D)-E-F[START]", false, 1, tmpFirstParticle, tmpLastParticle, tmpBondLength);
        tmpResult = tmpPSmiles.getParticlePositionsAndConnections();
        assertTrue("Test9.9.5", // New_Index
                tmpResult[0][0].equals("1")
                && tmpResult[0][1].equals("A")
                && tmpResult[0][2].equals("0")
                && tmpResult[0][3].equals("2.309401076758503")
                && tmpResult[0][4].equals("2.309401076758503")
                && tmpResult[0][5].equals("2.309401076758503")
                && tmpResult[0][6].equals("1") // New_Index
                && tmpResult[1][0].equals("2")
                && tmpResult[1][1].equals("B")
                && tmpResult[1][2].equals("0")
                && tmpResult[1][3].equals("1.7320508075688772")
                && tmpResult[1][4].equals("1.7320508075688772")
                && tmpResult[1][5].equals("1.7320508075688772")
                && tmpResult[1][6].equals("-1")
                && tmpResult[1][7].equals("1") // New_Index
                && tmpResult[2][0].equals("3")
                && tmpResult[2][1].equals("C")
                && tmpResult[2][2].equals("0")
                && tmpResult[2][3].equals("1.1547005383792515")
                && tmpResult[2][4].equals("1.1547005383792515")
                && tmpResult[2][5].equals("1.1547005383792515")
                && tmpResult[2][6].equals("-1")
                && tmpResult[2][7].equals("1")
                && tmpResult[2][8].equals("2") // New_Index
                && tmpResult[3][0].equals("4")
                && tmpResult[3][1].equals("D")
                && tmpResult[3][2].equals("0")
                && tmpResult[3][3].equals("1.1547005383792515")
                && tmpResult[3][4].equals("1.1547005383792515")
                && tmpResult[3][5].equals("1.1547005383792515")
                && tmpResult[3][6].equals("-1") // New_Index
                && tmpResult[4][0].equals("5")
                && tmpResult[4][1].equals("E")
                && tmpResult[4][2].equals("0")
                && tmpResult[4][3].equals("0.5773502691896257")
                && tmpResult[4][4].equals("0.5773502691896257")
                && tmpResult[4][5].equals("0.5773502691896257")
                && tmpResult[4][6].equals("-2")
                && tmpResult[4][7].equals("1") // New_Index
                && tmpResult[5][0].equals("6")
                && tmpResult[5][1].equals("F")
                && tmpResult[5][2].equals("0")
                && tmpResult[5][3].equals("0.0")
                && tmpResult[5][4].equals("0.0")
                && tmpResult[5][5].equals("0.0")
                && tmpResult[5][6].equals("-1"));

        tmpFirstParticle = new PointInSpace(0.0, 0.0, 0.0);
        tmpLastParticle = new PointInSpace(10.0, 10.0, 10.0);
        tmpBondLength = 1.0;
        tmpPSmiles = new PSmiles("A-B-C(D[START])-E[END]-F", false, 1, tmpFirstParticle, tmpLastParticle, tmpBondLength);
        tmpResult = tmpPSmiles.getParticlePositionsAndConnections();
        assertTrue("Test9.9.6", // New_Index
                tmpResult[0][0].equals("1")
                && tmpResult[0][1].equals("A")
                && tmpResult[0][2].equals("0")
                && tmpResult[0][3].equals("0.5773502691896257")
                && tmpResult[0][4].equals("0.5773502691896257")
                && tmpResult[0][5].equals("0.5773502691896257")
                && tmpResult[0][6].equals("1") // New_Index
                && tmpResult[1][0].equals("2")
                && tmpResult[1][1].equals("B")
                && tmpResult[1][2].equals("0")
                && tmpResult[1][3].equals("0.5773502691896257")
                && tmpResult[1][4].equals("0.5773502691896257")
                && tmpResult[1][5].equals("0.5773502691896257")
                && tmpResult[1][6].equals("-1")
                && tmpResult[1][7].equals("1") // New_Index
                && tmpResult[2][0].equals("3")
                && tmpResult[2][1].equals("C")
                && tmpResult[2][2].equals("0")
                && tmpResult[2][3].equals("0.5773502691896257")
                && tmpResult[2][4].equals("0.5773502691896257")
                && tmpResult[2][5].equals("0.5773502691896257")
                && tmpResult[2][6].equals("-1")
                && tmpResult[2][7].equals("1")
                && tmpResult[2][8].equals("2") // New_Index
                && tmpResult[3][0].equals("4")
                && tmpResult[3][1].equals("D")
                && tmpResult[3][2].equals("0")
                && tmpResult[3][3].equals("0.0")
                && tmpResult[3][4].equals("0.0")
                && tmpResult[3][5].equals("0.0")
                && tmpResult[3][6].equals("-1") // New_Index
                && tmpResult[4][0].equals("5")
                && tmpResult[4][1].equals("E")
                && tmpResult[4][2].equals("0")
                && tmpResult[4][3].equals("1.1547005383792515")
                && tmpResult[4][4].equals("1.1547005383792515")
                && tmpResult[4][5].equals("1.1547005383792515")
                && tmpResult[4][6].equals("-2")
                && tmpResult[4][7].equals("1") // New_Index
                && tmpResult[5][0].equals("6")
                && tmpResult[5][1].equals("F")
                && tmpResult[5][2].equals("0")
                && tmpResult[5][3].equals("1.1547005383792515")
                && tmpResult[5][4].equals("1.1547005383792515")
                && tmpResult[5][5].equals("1.1547005383792515")
                && tmpResult[5][6].equals("-1"));

        tmpPSmiles = new PSmiles("A-B-C(D[START])-E[END]-F");
        tmpResult = tmpPSmiles.getParticlePositionsAndConnections();

        // </editor-fold>
    }

    /**
     * Test of hasSubstructures property
     */
    public void testHasSubstrucutres() {
        PSmiles tmpPSmiles = new PSmiles("A-B-C");
        boolean tmpResult = tmpPSmiles.hasMultipleParts();
        assertFalse("Test9.5a.1", tmpResult);

        tmpPSmiles = new PSmiles("<A-B><C>");
        tmpResult = tmpPSmiles.hasMultipleParts();
        assertTrue("Test9.5a.2", tmpResult);
    }

    /**
     * Test of setInputStructure()
     */
    public void testSetInputStructure() {
        PSmiles tmpPSmiles = new PSmiles("A-B-C");
        String tmpResult = tmpPSmiles.getInputStructure();
        assertTrue("Test9.6.1", tmpResult.equals("A-B-C"));

        tmpPSmiles.setInputStructure("hugo");
        tmpResult = tmpPSmiles.getInputStructure();
        assertTrue("Test9.6.5", tmpResult.equals("hugo"));
        boolean tmpValid = tmpPSmiles.isValid();
        tmpResult = tmpPSmiles.getErrorMessage();
        assertTrue("Test9.6.6", tmpValid == false);
        String tmpExpectString = MessagePSmiles.getString("StructureCheck.InvalidParticlename");
        assertTrue("Test9.6.7", tmpResult.equals(tmpExpectString));

        HashMap<String, String> tmpAvailableParticles = new HashMap<>();
        tmpAvailableParticles.put("A", "A");
        tmpAvailableParticles.put("B", "B");
        boolean tmpIsMonomer = false;
        tmpPSmiles = new PSmiles("<A><B>", tmpAvailableParticles, tmpIsMonomer);
        tmpValid = tmpPSmiles.isValid();
        assertTrue("Test9.6.8", tmpValid == true);
        tmpResult = tmpPSmiles.getErrorMessage();
        assertTrue("Test9.6.9", tmpResult == null);
        tmpPSmiles.setInputStructure("<A><C>", tmpAvailableParticles, tmpIsMonomer);
        tmpValid = tmpPSmiles.isValid();
        assertTrue("Test9.6.10", tmpValid == false);
    }

    /**
     * Test of setCoordinates
     */
    public void testSetCorrdinates() {
        PSmiles tmpPSmiles = new PSmiles("A-B-C(D)-E-F");
        PointInSpace[] tmpFirstParticleCoordinate = new PointInSpace[1];
        PointInSpace[] tmpLastParticleCoordinate = new PointInSpace[1];
        tmpFirstParticleCoordinate[0] = new PointInSpace(0.0, 0.0, 0.0);
        tmpLastParticleCoordinate[0] = new PointInSpace(10.0, 10.0, 10.0);
        double tmpBondLength = 1.0;
        int tmpStartIndex = 0;
        tmpPSmiles.setCoordinates(tmpStartIndex, tmpFirstParticleCoordinate, tmpLastParticleCoordinate, tmpBondLength);
        IPointInSpace[][] tmpResult = tmpPSmiles.getParticleCoordinates(tmpFirstParticleCoordinate, tmpLastParticleCoordinate, tmpBondLength);
        assertTrue("Test9.7",
                tmpResult[0][0].getX() == 0.0
                && tmpResult[0][0].getY() == 0.0
                && tmpResult[0][0].getZ() == 0.0
                && tmpResult[0][1].getX() == 0.5773502691896257
                && tmpResult[0][1].getY() == 0.5773502691896257
                && tmpResult[0][1].getZ() == 0.5773502691896257
                && tmpResult[0][2].getX() == 1.1547005383792515
                && tmpResult[0][2].getY() == 1.1547005383792515
                && tmpResult[0][2].getZ() == 1.1547005383792515
                && tmpResult[0][3].getX() == 1.1547005383792515
                && tmpResult[0][3].getY() == 1.1547005383792515
                && tmpResult[0][3].getZ() == 1.1547005383792515
                && tmpResult[0][4].getX() == 1.7320508075688772
                && tmpResult[0][4].getY() == 1.7320508075688772
                && tmpResult[0][4].getZ() == 1.7320508075688772
                && tmpResult[0][5].getX() == 2.309401076758503
                && tmpResult[0][5].getY() == 2.309401076758503
                && tmpResult[0][5].getZ() == 2.309401076758503);

    }

    /**
     * Test of getBackboneIndices()
     */
    public void testGetBackboneIndices() {
        int[] tmpResult;
        String tmpResultString;
        String tmpExpectString;

        PSmiles tmpPSmiles = new PSmiles(null);
        tmpPSmiles = new PSmiles("Aa1'1'-B-B-C-D'2'");
        tmpResult = tmpPSmiles.getBackboneIndices();
        assertTrue("Test9A.1.1", tmpResult[0] == 1);
        assertTrue("Test9A.1.2", tmpResult[1] == 0);
        assertTrue("Test9A.1.2", tmpResult[2] == 0);
        assertTrue("Test9A.1.2", tmpResult[3] == 0);
        assertTrue("Test9A.1.2", tmpResult[4] == 2);
        assertTrue("Test9A.1.2", tmpPSmiles.hasBackboneParticle() == true);
        
        tmpPSmiles = new PSmiles("10A'1'-B-C'2'");
        tmpResult = tmpPSmiles.getBackboneIndices();
        assertTrue("Test9A.1.3", tmpResult[8] == 0);
        assertTrue("Test9A.1.3", tmpResult[9] == 1);
        assertTrue("Test9A.1.3", tmpResult[11] == 2);
        assertTrue("Test9A.1.3", tmpPSmiles.hasBackboneParticle() == true);

        tmpPSmiles = new PSmiles("Aa1-B-B-C-D");
        assertTrue("Test9A.2.2", tmpPSmiles.hasBackboneParticle() == false);

        tmpPSmiles = new PSmiles("Aa1'1'-B-B-C-D'2'");
        tmpResultString = tmpPSmiles.getErrorMessage();
        assertNull("Test9A.3", tmpResultString);

        tmpPSmiles = new PSmiles("Aa1'1'-B-B-C-D'1'");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.RedundancyOfBackboneIndices");
        assertEquals("Test9A.4", tmpResultString, tmpExpectString);

        tmpPSmiles = new PSmiles("Aa1'1'-B-B-C-D'3'");
        tmpResultString = tmpPSmiles.getErrorMessage();
        tmpExpectString = MessagePSmiles.getString("StructureCheck.MissingBackboneIndex");
        assertEquals("Test9A.5", tmpResultString, tmpExpectString);
    }

    /**
     * Test of getMaximumNumberOfConnectionsOfSingleParticle()
     */
    public void testGetMaximumNumberOfConnectionsOfSingleParticle() {
        PSmiles tmpPSmiles = new PSmiles("A-B");
        int tmpResult = tmpPSmiles.getMaximumNumberOfConnectionsOfSingleParticle();
        assertTrue("Test1", tmpResult == 1);

        tmpPSmiles = new PSmiles("A-B-C");
        tmpResult = tmpPSmiles.getMaximumNumberOfConnectionsOfSingleParticle();
        assertTrue("Test2", tmpResult == 2);

        tmpPSmiles = new PSmiles("A-B(C)-D");
        tmpResult = tmpPSmiles.getMaximumNumberOfConnectionsOfSingleParticle();
        assertTrue("Test3", tmpResult == 3);
    }
    
    // </editor-fold>
    //
    // <editor-fold defaultstate="collapsed" desc="Public methods">
    /**
     * Test of getNextOneNeighbor method
     */
    public void testGetNextNeighbor() {
        String[][] tmpResultString;

        // <editor-fold defaultstate="collapsed" desc="Simple tests">
        PSmiles tmpPSmiles = new PSmiles(null);
        tmpResultString = tmpPSmiles.getNextNeighbors(2, true);
        assertNull("Test8.1", tmpResultString);

        tmpPSmiles = new PSmiles("");
        tmpResultString = tmpPSmiles.getNextNeighbors(2, true);
        assertNull("Test8.2", tmpResultString);

        tmpPSmiles = new PSmiles("A");
        tmpResultString = tmpPSmiles.getNextNeighbors(2, true);
        assertTrue("Test8.3", tmpResultString[0][0].equals("A") && tmpResultString[1] == null);

        tmpPSmiles = new PSmiles("A-B-C");
        tmpResultString = tmpPSmiles.getNextNeighbors(2, true);
        Arrays.sort(tmpResultString[1]);
        assertTrue("Test8.4", tmpResultString[1][0].equals("A-B") && tmpResultString[1][1].equals("B-A") && tmpResultString[1][2].equals("B-C") && tmpResultString[1][3].equals("C-B")
                && tmpResultString[1].length == 4);

        tmpPSmiles = new PSmiles("C-C-B-A-B-A");
        tmpResultString = tmpPSmiles.getNextNeighbors(1, true);
        Arrays.sort(tmpResultString[0]);
        assertTrue("Test8.5", tmpResultString[0][0].equals("A") && tmpResultString[0][1].equals("B") && tmpResultString[0][2].equals("C") && tmpResultString[0].length == 3);

        tmpPSmiles = new PSmiles("A-B-C");
        tmpResultString = tmpPSmiles.getNextNeighbors(0, true);
        assertNull("Test8.6", tmpResultString);

        tmpPSmiles = new PSmiles("<H2O><H2O>");
        tmpResultString = tmpPSmiles.getNextNeighbors(1, true);
        assertTrue("Test8.7", tmpResultString[0][0].equals("H2O") && tmpResultString[0][1].equals("H2O"));

        tmpPSmiles = new PSmiles("H2O");
        tmpPSmiles.setInputStructure("<Ammonium><Nitrate>");
        tmpResultString = tmpPSmiles.getNextNeighbors(0, true);
        assertNull("Test8.8", tmpResultString);

        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Test with normal brackets">
        tmpPSmiles = new PSmiles("A(B)-C");
        tmpResultString = tmpPSmiles.getNextNeighbors(2, true);
        assertTrue("Test8.7.1", tmpResultString[1].length == 4);
        assertTrue("Test8.7.2", Arrays.asList(tmpResultString[1]).contains("A-B"));
        assertTrue("Test8.7.3", Arrays.asList(tmpResultString[1]).contains("A-C"));
        assertTrue("Test8.7.4", Arrays.asList(tmpResultString[1]).contains("B-A"));
        assertTrue("Test8.7.5", Arrays.asList(tmpResultString[1]).contains("C-A"));

        tmpPSmiles = new PSmiles("A(B)(C)-D");
        tmpResultString = tmpPSmiles.getNextNeighbors(2, true);
        assertTrue("Test8.8.1", tmpResultString[1].length == 6);
        assertTrue("Test8.8.2", Arrays.asList(tmpResultString[1]).contains("A-B"));
        assertTrue("Test8.8.3", Arrays.asList(tmpResultString[1]).contains("A-C"));
        assertTrue("Test8.8.4", Arrays.asList(tmpResultString[1]).contains("A-D"));
        assertTrue("Test8.8.5", Arrays.asList(tmpResultString[1]).contains("B-A"));
        assertTrue("Test8.8.6", Arrays.asList(tmpResultString[1]).contains("C-A"));
        assertTrue("Test8.8.7", Arrays.asList(tmpResultString[1]).contains("D-A"));

        tmpPSmiles = new PSmiles("A(B-C-D)(E1-E2)(F1)-D");
        tmpResultString = tmpPSmiles.getNextNeighbors(2, true);
        Arrays.sort(tmpResultString[1]);
        assertTrue("Test8.9", tmpResultString[1][0].equals("A-B") && tmpResultString[1][1].equals("A-D") && tmpResultString[1][2].equals("A-E1") && tmpResultString[1][3].equals("A-F1")
                && tmpResultString[1][4].equals("B-A") && tmpResultString[1][5].equals("B-C") && tmpResultString[1][6].equals("C-B") && tmpResultString[1][7].equals("C-D")
                && tmpResultString[1][8].equals("D-A") && tmpResultString[1][9].equals("D-C") && tmpResultString[1][10].equals("E1-A") && tmpResultString[1][11].equals("E1-E2")
                && tmpResultString[1][12].equals("E2-E1") && tmpResultString[1][13].equals("F1-A") && tmpResultString[1].length == 14);

        tmpPSmiles = new PSmiles("A(B-C(E1(F1)-E2)-D)-G");
        tmpResultString = tmpPSmiles.getNextNeighbors(2, true);
        Arrays.sort(tmpResultString[1]);
        assertTrue("Test8.10", tmpResultString[1][0].equals("A-B") && tmpResultString[1][1].equals("A-G") && tmpResultString[1][2].equals("B-A") && tmpResultString[1][3].equals("B-C")
                && tmpResultString[1][4].equals("C-B") && tmpResultString[1][5].equals("C-D") && tmpResultString[1][6].equals("C-E1") && tmpResultString[1][7].equals("D-C")
                && tmpResultString[1][8].equals("E1-C") && tmpResultString[1][9].equals("E1-E2") && tmpResultString[1][10].equals("E1-F1") && tmpResultString[1][11].equals("E2-E1")
                && tmpResultString[1][12].equals("F1-E1") && tmpResultString[1][13].equals("G-A") && tmpResultString[1].length == 14);

        tmpPSmiles = new PSmiles("A-B(B-C)-D-E(F1-F2(G)-F3)-G");
        tmpResultString = tmpPSmiles.getNextNeighbors(2, true);
        Arrays.sort(tmpResultString[1]);
        assertTrue("Test8.11", tmpResultString[1][0].equals("A-B") && tmpResultString[1][1].equals("B-A") && tmpResultString[1][2].equals("B-B") && tmpResultString[1][3].equals("B-C")
                && tmpResultString[1][4].equals("B-D") && tmpResultString[1][5].equals("C-B") && tmpResultString[1][6].equals("D-B") && tmpResultString[1][7].equals("D-E")
                && tmpResultString[1][8].equals("E-D") && tmpResultString[1][9].equals("E-F1") && tmpResultString[1][10].equals("E-G") && tmpResultString[1][11].equals("F1-E")
                && tmpResultString[1][12].equals("F1-F2") && tmpResultString[1][13].equals("F2-F1") && tmpResultString[1][14].equals("F2-F3") && tmpResultString[1][15].equals("F2-G")
                && tmpResultString[1][16].equals("F3-F2") && tmpResultString[1][17].equals("G-E") && tmpResultString[1][18].equals("G-F2") && tmpResultString[1].length == 19);

        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Test ring closures">
        tmpPSmiles = new PSmiles("A[1]-B-C[1]");
        tmpResultString = tmpPSmiles.getNextNeighbors(2, true);
        Arrays.sort(tmpResultString[1]);
        assertTrue("Test8.12", tmpResultString[1][0].equals("A-B") && tmpResultString[1][1].equals("A-C") && tmpResultString[1][2].equals("B-A") && tmpResultString[1][3].equals("B-C")
                && tmpResultString[1][4].equals("C-A") && tmpResultString[1][5].equals("C-B") && tmpResultString[1].length == 6);

        tmpPSmiles = new PSmiles("A[1]-B-C[2]-D[1]-E[2]");
        tmpResultString = tmpPSmiles.getNextNeighbors(2, true);
        Arrays.sort(tmpResultString[1]);
        assertTrue("Test8.13", tmpResultString[1][0].equals("A-B") && tmpResultString[1][1].equals("A-D") && tmpResultString[1][2].equals("B-A") && tmpResultString[1][3].equals("B-C")
                && tmpResultString[1][4].equals("C-B") && tmpResultString[1][5].equals("C-D") && tmpResultString[1][6].equals("C-E") && tmpResultString[1][7].equals("D-A")
                && tmpResultString[1][8].equals("D-C") && tmpResultString[1][9].equals("D-E") && tmpResultString[1][10].equals("E-C") && tmpResultString[1][11].equals("E-D")
                && tmpResultString[1].length == 12);

        tmpPSmiles = new PSmiles("A[1][2][3]-B-C-D[1]-E[2]-F[3]");
        tmpResultString = tmpPSmiles.getNextNeighbors(2, true);
        Arrays.sort(tmpResultString[1]);
        assertTrue("Test8.14", tmpResultString[1][0].equals("A-B") && tmpResultString[1][1].equals("A-D") && tmpResultString[1][2].equals("A-E") && tmpResultString[1][3].equals("A-F")
                && tmpResultString[1][4].equals("B-A") && tmpResultString[1][5].equals("B-C") && tmpResultString[1][6].equals("C-B") && tmpResultString[1][7].equals("C-D")
                && tmpResultString[1][8].equals("D-A") && tmpResultString[1][9].equals("D-C") && tmpResultString[1][10].equals("D-E") && tmpResultString[1][11].equals("E-A")
                && tmpResultString[1][12].equals("E-D") && tmpResultString[1][13].equals("E-F") && tmpResultString[1][14].equals("F-A") && tmpResultString[1][15].equals("F-E")
                && tmpResultString[1].length == 16);

        tmpPSmiles = new PSmiles("A[1][2][3]-B-C-D[1][2][3]");
        tmpResultString = tmpPSmiles.getNextNeighbors(2, true);
        Arrays.sort(tmpResultString[1]);
        assertTrue("Test8.15", tmpResultString[1][0].equals("A-B") && tmpResultString[1][1].equals("A-D") && tmpResultString[1][2].equals("B-A") && tmpResultString[1][3].equals("B-C")
                && tmpResultString[1][4].equals("C-B") && tmpResultString[1][5].equals("C-D") && tmpResultString[1][6].equals("D-A") && tmpResultString[1][7].equals("D-C")
                && tmpResultString[1].length == 8);

        tmpPSmiles = new PSmiles("A[1]-B1(C-B3[2](D[3]))-B4[2]-B2[1][3]");
        tmpResultString = tmpPSmiles.getNextNeighbors(2, true);
        Arrays.sort(tmpResultString[1]);
        assertTrue("Test8.16", tmpResultString[1][0].equals("A-B1") && tmpResultString[1][1].equals("A-B2") && tmpResultString[1][2].equals("B1-A") && tmpResultString[1][3].equals("B1-B4")
                && tmpResultString[1][4].equals("B1-C") && tmpResultString[1][5].equals("B2-A") && tmpResultString[1][6].equals("B2-B4") && tmpResultString[1][7].equals("B2-D")
                && tmpResultString[1][8].equals("B3-B4") && tmpResultString[1][9].equals("B3-C") && tmpResultString[1][10].equals("B3-D") && tmpResultString[1][11].equals("B4-B1")
                && tmpResultString[1][12].equals("B4-B2") && tmpResultString[1][13].equals("B4-B3") && tmpResultString[1][14].equals("C-B1") && tmpResultString[1][15].equals("C-B3")
                && tmpResultString[1][16].equals("D-B2") && tmpResultString[1][17].equals("D-B3") && tmpResultString[1].length == 18);

        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Test monomers">
        tmpPSmiles = new PSmiles("{A[HEAD]-B[TAIL]}");
        tmpResultString = tmpPSmiles.getNextNeighbors(2, true);
        Arrays.sort(tmpResultString[1]);
        assertTrue("Test8.19", tmpResultString[1][0].equals("A-B") && tmpResultString[1][1].equals("B-A") && tmpResultString[1].length == 2);

        tmpPSmiles = new PSmiles("{A[HEAD]-C(B[TAIL])}");
        tmpResultString = tmpPSmiles.getNextNeighbors(2, true);
        Arrays.sort(tmpResultString[1]);
        assertTrue("Test8.20", tmpResultString[1][0].equals("A-C") && tmpResultString[1][1].equals("B-C") && tmpResultString[1][2].equals("C-A") && tmpResultString[1][3].equals("C-B")
                && tmpResultString[1].length == 4);

        tmpPSmiles = new PSmiles("A-{B[HEAD]-C-D[TAIL]}-E");
        tmpResultString = tmpPSmiles.getNextNeighbors(2, true);
        Arrays.sort(tmpResultString[1]);
        assertTrue("Test8.21", tmpResultString[1][0].equals("A-B") && tmpResultString[1][1].equals("B-A") && tmpResultString[1][2].equals("B-C") && tmpResultString[1][3].equals("C-B")
                && tmpResultString[1][4].equals("C-D") && tmpResultString[1][5].equals("D-C") && tmpResultString[1][6].equals("D-E") && tmpResultString[1][7].equals("E-D")
                && tmpResultString[1].length == 8);

        tmpPSmiles = new PSmiles("A-{B-C[TAIL]-D[HEAD]-E}-F");
        tmpResultString = tmpPSmiles.getNextNeighbors(2, true);
        Arrays.sort(tmpResultString[1]);
        assertTrue("Test8.22", tmpResultString[1][0].equals("A-D") && tmpResultString[1][1].equals("B-C") && tmpResultString[1][2].equals("C-B") && tmpResultString[1][3].equals("C-D")
                && tmpResultString[1][4].equals("C-F") && tmpResultString[1][5].equals("D-A") && tmpResultString[1][6].equals("D-C") && tmpResultString[1][7].equals("D-E")
                && tmpResultString[1][8].equals("E-D") && tmpResultString[1][9].equals("F-C") && tmpResultString[1].length == 10);

        tmpPSmiles = new PSmiles("{C[TAIL]-A[HEAD]-B}-{E-D[HEAD]-F[TAIL]}-G");
        tmpResultString = tmpPSmiles.getNextNeighbors(2, true);
        Arrays.sort(tmpResultString[1]);
        assertTrue("Test8.23", tmpResultString[1][0].equals("A-B") && tmpResultString[1][1].equals("A-C") && tmpResultString[1][2].equals("B-A") && tmpResultString[1][3].equals("C-A")
                && tmpResultString[1][4].equals("C-D") && tmpResultString[1][5].equals("D-C") && tmpResultString[1][6].equals("D-E") && tmpResultString[1][7].equals("D-F")
                && tmpResultString[1][8].equals("E-D") && tmpResultString[1][9].equals("F-D") && tmpResultString[1][10].equals("F-G") && tmpResultString[1][11].equals("G-F")
                && tmpResultString[1].length == 12);

        tmpPSmiles = new PSmiles("{C[TAIL]-A[HEAD]-B}-H-I-{E-D[HEAD]-F[TAIL]}-G");
        tmpResultString = tmpPSmiles.getNextNeighbors(2, true);
        Arrays.sort(tmpResultString[1]);
        assertTrue("Test8.24", tmpResultString[1][0].equals("A-B") && tmpResultString[1][1].equals("A-C") && tmpResultString[1][2].equals("B-A") && tmpResultString[1][3].equals("C-A")
                && tmpResultString[1][4].equals("C-H") && tmpResultString[1][5].equals("D-E") && tmpResultString[1][6].equals("D-F") && tmpResultString[1][7].equals("D-I")
                && tmpResultString[1][8].equals("E-D") && tmpResultString[1][9].equals("F-D") && tmpResultString[1][10].equals("F-G") && tmpResultString[1][11].equals("G-F")
                && tmpResultString[1][12].equals("H-C") && tmpResultString[1][13].equals("H-I") && tmpResultString[1][14].equals("I-D") && tmpResultString[1][15].equals("I-H")
                && tmpResultString[1].length == 16);

        tmpPSmiles = new PSmiles("{C[TAIL]-A[HEAD]-B}-#Butadien-{E-D[HEAD]-F[TAIL]}-#Ethylen");
        tmpResultString = tmpPSmiles.getNextNeighbors(2, true);
        Arrays.sort(tmpResultString[1]);
        assertTrue("Test8.25", tmpResultString[1][0].equals("#Butadien-C") && tmpResultString[1][1].equals("#Butadien-D") && tmpResultString[1][2].equals("#Ethylen-F")
                && tmpResultString[1][3].equals("A-B") && tmpResultString[1][4].equals("A-C") && tmpResultString[1][5].equals("B-A") && tmpResultString[1][6].equals("C-#Butadien")
                && tmpResultString[1][7].equals("C-A") && tmpResultString[1][8].equals("D-#Butadien") && tmpResultString[1][9].equals("D-E") && tmpResultString[1][10].equals("D-F")
                && tmpResultString[1][11].equals("E-D") && tmpResultString[1][12].equals("F-#Ethylen") && tmpResultString[1][13].equals("F-D") && tmpResultString[1].length == 14);

        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Test repetition">
        tmpPSmiles = new PSmiles("3A");
        tmpResultString = tmpPSmiles.getNextNeighbors(2, true);
        assertTrue("Test8.26", tmpResultString[1][0].equals("A-A") && tmpResultString[1].length == 1);

        tmpPSmiles = new PSmiles("A-3B-C");
        tmpResultString = tmpPSmiles.getNextNeighbors(2, true);
        Arrays.sort(tmpResultString[1]);
        assertTrue("Test8.27", tmpResultString[1][0].equals("A-B") && tmpResultString[1][1].equals("B-A") && tmpResultString[1][2].equals("B-B") && tmpResultString[1][3].equals("B-C")
                && tmpResultString[1][4].equals("C-B") && tmpResultString[1].length == 5);

        tmpPSmiles = new PSmiles("A(3B)-C");
        tmpResultString = tmpPSmiles.getNextNeighbors(2, true);
        Arrays.sort(tmpResultString[1]);
        assertTrue("Test8.28", tmpResultString[1][0].equals("A-B") && tmpResultString[1][1].equals("A-C") && tmpResultString[1][2].equals("B-A") && tmpResultString[1][3].equals("B-B")
                && tmpResultString[1][4].equals("C-A") && tmpResultString[1].length == 5);

        tmpPSmiles = new PSmiles("4{A[HEAD]-B-C[TAIL]}");
        tmpResultString = tmpPSmiles.getNextNeighbors(2, true);
        Arrays.sort(tmpResultString[1]);
        assertTrue("Test8.29", tmpResultString[1][0].equals("A-B") && tmpResultString[1][1].equals("A-C") && tmpResultString[1][2].equals("B-A") && tmpResultString[1][3].equals("B-C")
                && tmpResultString[1][4].equals("C-A") && tmpResultString[1][5].equals("C-B") && tmpResultString[1].length == 6);

        tmpPSmiles = new PSmiles("A-4{B[HEAD]-C[TAIL]}-D");
        tmpResultString = tmpPSmiles.getNextNeighbors(2, true);
        Arrays.sort(tmpResultString[1]);
        assertTrue("Test8.30", tmpResultString[1][0].equals("A-B") && tmpResultString[1][1].equals("B-A") && tmpResultString[1][2].equals("B-C") && tmpResultString[1][3].equals("C-B")
                && tmpResultString[1][4].equals("C-D") && tmpResultString[1][5].equals("D-C") && tmpResultString[1].length == 6);

        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Test find N-mers">
        tmpPSmiles = new PSmiles("A-B-C");
        tmpResultString = tmpPSmiles.getNextNeighbors(3, true);
        Arrays.sort(tmpResultString[2]);
        assertTrue("Test8.31", tmpResultString[2][0].equals("A-B-C") && tmpResultString[2][1].equals("C-B-A") && tmpResultString[2].length == 2);

        tmpPSmiles = new PSmiles("A[1]-B-C[1]");
        tmpResultString = tmpPSmiles.getNextNeighbors(3, true);
        Arrays.sort(tmpResultString[2]);
        assertTrue("Test8.32", tmpResultString[2][0].equals("A-B-C") && tmpResultString[2][1].equals("A-C-B") && tmpResultString[2][2].equals("B-A-C") && tmpResultString[2][3].equals("B-C-A")
                && tmpResultString[2][4].equals("C-A-B") && tmpResultString[2][5].equals("C-B-A") && tmpResultString[2].length == 6);

        tmpPSmiles = new PSmiles("A(B-C)(D)-E");
        tmpResultString = tmpPSmiles.getNextNeighbors(3, true);
        Arrays.sort(tmpResultString[2]);
        assertTrue("Test8.33", tmpResultString[2][0].equals("A-B-C") && tmpResultString[2][1].equals("B-A-D") && tmpResultString[2][2].equals("B-A-E") && tmpResultString[2][3].equals("C-B-A")
                && tmpResultString[2][4].equals("D-A-B") && tmpResultString[2][5].equals("D-A-E") && tmpResultString[2][6].equals("E-A-B") && tmpResultString[2][7].equals("E-A-D")
                && tmpResultString[2].length == 8);

        tmpPSmiles = new PSmiles("A(B-C)(D)-E");
        tmpResultString = tmpPSmiles.getNextNeighbors(4, true);
        Arrays.sort(tmpResultString[3]);
        assertTrue("Test8.34", tmpResultString[3][0].equals("C-B-A-D") && tmpResultString[3][1].equals("C-B-A-E") && tmpResultString[3][2].equals("D-A-B-C") && tmpResultString[3][3].equals("E-A-B-C")
                && tmpResultString[3].length == 4);

        tmpPSmiles = new PSmiles("A(B-C)(D)-E");
        tmpResultString = tmpPSmiles.getNextNeighbors(5, true);
        assertTrue("Test8.35", tmpResultString[4] == null);

        tmpPSmiles = new PSmiles("A[1]-B-C-D-E[1]");
        tmpResultString = tmpPSmiles.getNextNeighbors(6, true);
        assertTrue("Test8.36", tmpResultString[4].length == 10);

        tmpPSmiles = new PSmiles("3A");
        tmpResultString = tmpPSmiles.getNextNeighbors(3, true);
        assertTrue("Test8.37", tmpResultString[2][0].equals("A-A-A"));

        tmpPSmiles = new PSmiles("3A");
        tmpResultString = tmpPSmiles.getNextNeighbors(4, true);
        assertTrue("Test8.38", tmpResultString[3] == null);

        tmpPSmiles = new PSmiles("A-3B-C");
        tmpResultString = tmpPSmiles.getNextNeighbors(4, true);
        Arrays.sort(tmpResultString[3]);
        assertTrue("Test8.39", tmpResultString[3][0].equals("A-B-B-B") && tmpResultString[3][1].equals("B-B-B-A") && tmpResultString[3][2].equals("B-B-B-C") && tmpResultString[3][3].equals("C-B-B-B")
                && tmpResultString[3].length == 4);

        tmpPSmiles = new PSmiles("2{A[TAIL]-B[HEAD]}");
        tmpResultString = tmpPSmiles.getNextNeighbors(2, true);
        Arrays.sort(tmpResultString[1]);
        assertTrue("Test8.40", tmpResultString[1][0].equals("A-B") && tmpResultString[1][1].equals("B-A") && tmpResultString[1].length == 2);

        tmpPSmiles = new PSmiles("2{A[TAIL]-B[HEAD]}");
        tmpResultString = tmpPSmiles.getNextNeighbors(3, true);
        Arrays.sort(tmpResultString[2]);
        assertTrue("Test8.41", tmpResultString[2][0].equals("A-B-A") && tmpResultString[2][1].equals("B-A-B") && tmpResultString[2].length == 2);

        tmpPSmiles = new PSmiles("2{A[TAIL]-B[HEAD]}");
        tmpResultString = tmpPSmiles.getNextNeighbors(4, true);
        assertTrue("Test8.42", tmpResultString[3][0].equals("B-A-B-A") && tmpResultString[3][1].equals("A-B-A-B") && tmpResultString[3].length == 2);

        tmpPSmiles = new PSmiles("A-2{B-C[HEAD]-3D-E[TAIL]}");
        tmpResultString = tmpPSmiles.getNextNeighbors(3, true);
        Arrays.sort(tmpResultString[2]);
        assertTrue("Test8.43", tmpResultString[2][0].equals("A-C-B") && tmpResultString[2][1].equals("A-C-D") && tmpResultString[2][2].equals("B-C-A") && tmpResultString[2][3].equals("B-C-D")
                && tmpResultString[2][4].equals("B-C-E") && tmpResultString[2][5].equals("C-D-D") && tmpResultString[2][6].equals("C-E-D") && tmpResultString[2][7].equals("D-C-A")
                && tmpResultString[2][8].equals("D-C-B") && tmpResultString[2][9].equals("D-C-E") && tmpResultString[2][10].equals("D-D-C") && tmpResultString[2][11].equals("D-D-D")
                && tmpResultString[2][12].equals("D-D-E") && tmpResultString[2][13].equals("D-E-C") && tmpResultString[2][14].equals("E-C-B") && tmpResultString[2][15].equals("E-C-D")
                && tmpResultString[2][16].equals("E-D-D") && tmpResultString[2].length == 17);

        tmpPSmiles = new PSmiles("10A");
        tmpResultString = tmpPSmiles.getNextNeighbors(9, true);
        assertTrue("Test8.44", tmpResultString[8][0].equals("A-A-A-A-A-A-A-A-A") && tmpResultString[8].length == 1);

        tmpPSmiles = new PSmiles("A(B-C[1])(B-C[2])-B-C-D[1][2]");
        tmpResultString = tmpPSmiles.getNextNeighbors(3, true);
        Arrays.sort(tmpResultString[2]);
        assertTrue("Test8.45", tmpResultString[2][0].equals("A-B-C") && tmpResultString[2][1].equals("B-A-B") && tmpResultString[2][2].equals("B-C-D") && tmpResultString[2][3].equals("C-B-A")
                && tmpResultString[2][4].equals("C-D-C") && tmpResultString[2][5].equals("D-C-B") && tmpResultString[2].length == 6);

        // N-mers with parts
        tmpPSmiles = new PSmiles("<6Et-MeOH-CisButene-Pr-Et-ZnAc-Et-Pr-CisButene-MeOH-6Et><TriMeNH2(HAcN)(HAcN)(HAcN)>");
        tmpResultString = tmpPSmiles.getNextNeighbors(4, true);
        assertNotNull("TEst8.45a", tmpResultString);
        // Result is not allowed to be null!

        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Test No Doublet">
        tmpPSmiles = new PSmiles("A-2{B-C[HEAD]-3D-E[TAIL]}");
        tmpResultString = tmpPSmiles.getNextNeighbors(3, false);
        Arrays.sort(tmpResultString[2]);
        assertTrue("Test8.46", tmpResultString[2][0].equals("A-C-B") && tmpResultString[2][1].equals("A-C-D") && tmpResultString[2][2].equals("B-C-D") && tmpResultString[2][3].equals("B-C-E")
                && tmpResultString[2][4].equals("C-D-D") && tmpResultString[2][5].equals("C-E-D") && tmpResultString[2][6].equals("D-C-E") && tmpResultString[2][7].equals("D-D-D")
                && tmpResultString[2][8].equals("D-D-E") && tmpResultString[2].length == 9);

        // </editor-fold>
    }

    /**
     * Test of GetCoordinates
     */
    public void testGetCoordinates() {
        String tmpStructure = "A-B-C(D)-E-F";
        PointInSpace tmpFirstParticleCoordinate = new PointInSpace(0.0, 0.0, 0.0);
        PointInSpace tmpLastParticleCoordinate = new PointInSpace(10.0, 10.0, 10.0);
        double tmpBondLength = 1.0;
        PSmiles pSmiles = new PSmiles(tmpStructure);
        IPointInSpace[][] tmpResult = pSmiles.getParticleCoordinates(tmpFirstParticleCoordinate, tmpLastParticleCoordinate, tmpBondLength);
        assertTrue("Test900",
                tmpResult[0][0].getX() == 0.0
                && tmpResult[0][0].getY() == 0.0
                && tmpResult[0][0].getZ() == 0.0
                && tmpResult[0][1].getX() == 0.5773502691896257
                && tmpResult[0][1].getY() == 0.5773502691896257
                && tmpResult[0][1].getZ() == 0.5773502691896257
                && tmpResult[0][2].getX() == 1.1547005383792515
                && tmpResult[0][2].getY() == 1.1547005383792515
                && tmpResult[0][2].getZ() == 1.1547005383792515
                && tmpResult[0][3].getX() == 1.1547005383792515
                && tmpResult[0][3].getY() == 1.1547005383792515
                && tmpResult[0][3].getZ() == 1.1547005383792515
                && tmpResult[0][4].getX() == 1.7320508075688772
                && tmpResult[0][4].getY() == 1.7320508075688772
                && tmpResult[0][4].getZ() == 1.7320508075688772
                && tmpResult[0][5].getX() == 2.309401076758503
                && tmpResult[0][5].getY() == 2.309401076758503
                && tmpResult[0][5].getZ() == 2.309401076758503);

        tmpStructure = "A-B-C(D[START])-E[END]-F";
        tmpFirstParticleCoordinate = new PointInSpace(0.0, 0.0, 0.0);
        tmpLastParticleCoordinate = new PointInSpace(10.0, 10.0, 10.0);
        tmpBondLength = 1.0;
        pSmiles = new PSmiles(tmpStructure);
        tmpResult = pSmiles.getParticleCoordinates(tmpFirstParticleCoordinate, tmpLastParticleCoordinate, tmpBondLength);
        assertTrue("Test901",
                tmpResult[0][0].getX() == 0.5773502691896257
                && tmpResult[0][0].getY() == 0.5773502691896257
                && tmpResult[0][0].getZ() == 0.5773502691896257
                && tmpResult[0][1].getX() == 0.5773502691896257
                && tmpResult[0][1].getY() == 0.5773502691896257
                && tmpResult[0][1].getZ() == 0.5773502691896257
                && tmpResult[0][2].getX() == 0.5773502691896257
                && tmpResult[0][2].getY() == 0.5773502691896257
                && tmpResult[0][2].getZ() == 0.5773502691896257
                && tmpResult[0][3].getX() == 0.0
                && tmpResult[0][3].getY() == 0.0
                && tmpResult[0][3].getZ() == 0.0
                && tmpResult[0][4].getX() == 1.1547005383792515
                && tmpResult[0][4].getY() == 1.1547005383792515
                && tmpResult[0][4].getZ() == 1.1547005383792515
                && tmpResult[0][5].getX() == 1.1547005383792515
                && tmpResult[0][5].getY() == 1.1547005383792515
                && tmpResult[0][5].getZ() == 1.1547005383792515);

        tmpStructure = "Methan[START]-11Methan-6DME-MeOH[END]";
        tmpFirstParticleCoordinate = new PointInSpace(0.0, 0.0, 0.0);
        tmpLastParticleCoordinate = new PointInSpace(10.0, 10.0, 10.0);
        tmpBondLength = 1.0;
        pSmiles = new PSmiles(tmpStructure);
        tmpResult = pSmiles.getParticleCoordinates(tmpFirstParticleCoordinate, tmpLastParticleCoordinate, tmpBondLength);

        tmpStructure = "Methan(EtAcetate-14Methan)-Methan(EtAcetate-6Methan-CisButen-8Methan)-Methan-DMP-Ethylamin";
        PSmiles tmpPSmiles = new PSmiles(tmpStructure);
        tmpFirstParticleCoordinate = new PointInSpace(0.0, 0.0, 0.0);
        tmpLastParticleCoordinate = new PointInSpace(10.0, 10.0, 10.0);
        tmpBondLength = 1.0;
        tmpResult = tmpPSmiles.getParticleCoordinates(tmpFirstParticleCoordinate, tmpLastParticleCoordinate, tmpBondLength);
        assertTrue("Test901a", tmpResult[0].length == 36
                && tmpResult[0][33].getX() == tmpResult[0][34].getX()
                && tmpResult[0][33].getX() == tmpResult[0][35].getX());

        tmpStructure = "Methan(EtAcetate-14Methan)-Methan(EtAcetate-6Methan-CisButen-8Methan)-Methan-DMP-Methan-MeOH-MeOH";
        tmpPSmiles = new PSmiles(tmpStructure);
        tmpResult = tmpPSmiles.getParticleCoordinates(tmpFirstParticleCoordinate, tmpLastParticleCoordinate, tmpBondLength);
        assertTrue("Test901b", tmpResult[0].length == 38);

        tmpStructure = "Methan(EtAcetate-6Methan-CisButen-Methan-CisButen-5Methan)-Methan(EtAcetate-6Methan-CisButen-Methan-CisButen-5Methan)-Methan-DMP-Methan-MeOH-Methan-DMP-Methan-Methan(EtAcetate-6Methan-CisButen-Methan-CisButen-5Methan)-Methan(EtAcetate-6Methan-CisButen-Methan-CisButen-5Methan)";
        tmpPSmiles = new PSmiles(tmpStructure);
        tmpResult = tmpPSmiles.getParticleCoordinates(tmpFirstParticleCoordinate, tmpLastParticleCoordinate, tmpBondLength);
        assertTrue("Test901c", tmpResult[0].length == 71);

        tmpStructure = "<A-B><C-D-E>";
        tmpPSmiles = new PSmiles(tmpStructure);
        tmpResult = tmpPSmiles.getParticleCoordinates(tmpFirstParticleCoordinate, tmpLastParticleCoordinate, tmpBondLength);
        assertTrue("Test902", tmpResult[0].length == 5
                && tmpResult[0][0].getX() == 0.0
                && tmpResult[0][0].getY() == 0.0
                && tmpResult[0][0].getZ() == 0.0
                && tmpResult[0][1].getX() == 0.5773502691896257
                && tmpResult[0][1].getY() == 0.5773502691896257
                && tmpResult[0][1].getZ() == 0.5773502691896257
                && tmpResult[0][2].getX() == 0.0
                && tmpResult[0][2].getY() == 0.0
                && tmpResult[0][2].getZ() == 0.0
                && tmpResult[0][3].getX() == 0.5773502691896257
                && tmpResult[0][3].getY() == 0.5773502691896257
                && tmpResult[0][3].getZ() == 0.5773502691896257
                && tmpResult[0][4].getX() == 1.1547005383792515
                && tmpResult[0][4].getY() == 1.1547005383792515
                && tmpResult[0][4].getZ() == 1.1547005383792515);
    }

    /**
     * Test of the public method getParticleToFrequencyMap
     */
    public void testGetParticleToFrequencyMap() {
        String tmpStructure = "A-B(A)-A-B";
        HashMap<String, ParticleFrequency> tmpResultHashMap;
        PSmiles pSmiles = new PSmiles(tmpStructure);
        tmpResultHashMap = pSmiles.getParticleToFrequencyMap();
        assertTrue("Test1000a", tmpResultHashMap.size() == 2);
        assertTrue("Test1000b", tmpResultHashMap.get("A").getFrequency() == 3);
        assertTrue("Test1000c", tmpResultHashMap.get("B").getFrequency() == 2);

        tmpStructure = "2<A-B><A-B-C>";
        pSmiles = new PSmiles(tmpStructure);
        tmpResultHashMap = pSmiles.getParticleToFrequencyMap();
        assertTrue("Test1001a", tmpResultHashMap.size() == 3);
        assertTrue("Test1001b", tmpResultHashMap.get("A").getFrequency() == 3);
        assertTrue("Test1001c", tmpResultHashMap.get("B").getFrequency() == 3);
        assertTrue("Test1001d", tmpResultHashMap.get("C").getFrequency() == 1);

        tmpStructure = "{A[HEAD]-2B-C[TAIL]}";
        pSmiles = new PSmiles(tmpStructure);
        tmpResultHashMap = pSmiles.getParticleToFrequencyMap();
        assertTrue("Test1002a", tmpResultHashMap.size() == 3);
        assertTrue("Test1002b", tmpResultHashMap.get("A").getFrequency() == 1);
        assertTrue("Test1002c", tmpResultHashMap.get("B").getFrequency() == 2);
        assertTrue("Test1002d", tmpResultHashMap.get("C").getFrequency() == 1);

        tmpStructure = "2{A[HEAD]-2B-C[TAIL]}";
        pSmiles = new PSmiles(tmpStructure);
        tmpResultHashMap = pSmiles.getParticleToFrequencyMap();
        assertTrue("Test1002a", tmpResultHashMap.size() == 3);
        assertTrue("Test1002b", tmpResultHashMap.get("A").getFrequency() == 2);
        assertTrue("Test1002c", tmpResultHashMap.get("B").getFrequency() == 4);
        assertTrue("Test1002d", tmpResultHashMap.get("C").getFrequency() == 2);

        // Molecule A
        pSmiles = new PSmiles("A");
        assertTrue("Test1", pSmiles.getTotalNumberOfParticles() == 1);
        assertTrue("Test2", pSmiles.getNumberOfDifferentParticles() == 1);
        assertTrue("Test3", pSmiles.getFrequencyOfSpecifiedParticle("A") == 1);
        assertTrue("Test4", pSmiles.getParticleFrequencies().length == 1);
        assertTrue("Test5", pSmiles.getParticleFrequencies()[0].getParticle().equals("A"));
        assertTrue("Test6", pSmiles.getParticleFrequencies()[0].getFrequency() == 1);
        assertTrue("Test7", pSmiles.hasParticle("A"));
        assertFalse("Test8", pSmiles.hasParticle("B"));

        // Molecule A-A-A
        pSmiles = new PSmiles("A-A-A");
        assertTrue("Test1", pSmiles.getTotalNumberOfParticles() == 3);
        assertTrue("Test2", pSmiles.getNumberOfDifferentParticles() == 1);
        assertTrue("Test3", pSmiles.getFrequencyOfSpecifiedParticle("A") == 3);
        assertTrue("Test4", pSmiles.getParticleFrequencies().length == 1);
        assertTrue("Test5", pSmiles.getParticleFrequencies()[0].getParticle().equals("A"));
        assertTrue("Test6", pSmiles.getParticleFrequencies()[0].getFrequency() == 3);
        assertTrue("Test7", pSmiles.hasParticle("A"));
        assertFalse("Test8", pSmiles.hasParticle("B"));

        // Molecule A-6B-A
        pSmiles = new PSmiles("A-6B-A");
        assertTrue("Test1", pSmiles.getTotalNumberOfParticles() == 8);
        assertTrue("Test2", pSmiles.getNumberOfDifferentParticles() == 2);
        assertTrue("Test3a", pSmiles.getFrequencyOfSpecifiedParticle("A") == 2);
        assertTrue("Test3b", pSmiles.getFrequencyOfSpecifiedParticle("B") == 6);
        assertTrue("Test4", pSmiles.getSortedParticleFrequencies().length == 2);
        assertTrue("Test5a", pSmiles.getSortedParticleFrequencies()[0].getParticle().equals("A"));
        assertTrue("Test5b", pSmiles.getSortedParticleFrequencies()[1].getParticle().equals("B"));
        assertTrue("Test6a", pSmiles.getSortedParticleFrequencies()[0].getFrequency() == 2);
        assertTrue("Test6b", pSmiles.getSortedParticleFrequencies()[1].getFrequency() == 6);
        assertTrue("Test7", pSmiles.hasParticle("A"));
        assertFalse("Test8", pSmiles.hasParticle("C"));

        // Molecule A-2B-3{4A-B[HEAD]-C-D[TAIL]}-6B-A
        pSmiles = new PSmiles("A-2B-3{4A-B[HEAD]-C-D[TAIL]}-6B-A");
        assertTrue("Test1", pSmiles.getTotalNumberOfParticles() == 31);
        assertTrue("Test2", pSmiles.getNumberOfDifferentParticles() == 4);
        assertTrue("Test3a", pSmiles.getFrequencyOfSpecifiedParticle("A") == 14);
        assertTrue("Test3b", pSmiles.getFrequencyOfSpecifiedParticle("B") == 11);
        assertTrue("Test3b", pSmiles.getFrequencyOfSpecifiedParticle("C") == 3);
        assertTrue("Test3b", pSmiles.getFrequencyOfSpecifiedParticle("D") == 3);
        assertTrue("Test4", pSmiles.getSortedParticleFrequencies().length == 4);
        assertTrue("Test5a", pSmiles.getSortedParticleFrequencies()[0].getParticle().equals("A"));
        assertTrue("Test5b", pSmiles.getSortedParticleFrequencies()[1].getParticle().equals("B"));
        assertTrue("Test5c", pSmiles.getSortedParticleFrequencies()[2].getParticle().equals("C"));
        assertTrue("Test5d", pSmiles.getSortedParticleFrequencies()[3].getParticle().equals("D"));
        assertTrue("Test6a", pSmiles.getSortedParticleFrequencies()[0].getFrequency() == 14);
        assertTrue("Test6b", pSmiles.getSortedParticleFrequencies()[1].getFrequency() == 11);
        assertTrue("Test6c", pSmiles.getSortedParticleFrequencies()[2].getFrequency() == 3);
        assertTrue("Test6d", pSmiles.getSortedParticleFrequencies()[3].getFrequency() == 3);
        assertTrue("Test7", pSmiles.hasParticle("A"));
        assertFalse("Test8", pSmiles.hasParticle("F"));
    }

    /** 
     * Test of the public method getPathStartToEnd
     */
    public void testGetPathStartToEnd() {
        String tmpStructure = "<A'1'[START](B4-C[1])(C[END]-A'2')-D[1]-3E-4{A[HEAD]-B[TAIL]}><H2O-#Hugo>";
        PSmiles pSmiles = new PSmiles(tmpStructure);
        PSmilesInner[] parts = pSmiles.getPartsOfPSmiles();
        int[] tmpPath = parts[0].getPathStartToEnd();
        assertTrue("Test1010a", tmpPath[0] == 0);
        assertTrue("Test1010b", tmpPath[1] == 3);
        tmpPath = parts[1].getPathStartToEnd();
        assertNull("Test1010c", tmpPath);
    }
    
    /**
     * Test of performance problems
     */
//    public void testPerformanceProblems() {
//        
//        // IMPORTANT: Edit tmpFilePathname to your test file location
//        String tmpFilePathname = "P:\\MFsim\\Supplement\\Mirco Daniel - PSmiles-Klasse\\Files for Testing\\1S58_QSmiles.txt";
//        String tmpInputStructure = this.readTextFileIntoSingleString(tmpFilePathname);
//        if (tmpInputStructure != null && !tmpInputStructure.isEmpty()) {
//            // The following statement takes about 5 minutes
//            System.out.println("Before: PSmiles tmpPSmiles = new PSmiles(tmpInputStructure);");
//            PSmiles tmpPSmiles = new PSmiles(tmpInputStructure);
//            System.out.println("After: PSmiles tmpPSmiles = new PSmiles(tmpInputStructure);");
//            assertTrue("Performance Test 01", true);
//        } else {
//            assertTrue("Performance Test 01", false);
//        }
//
//    }
    // <editor-fold defaultstate="collapsed" desc="Commented code">
//    /**
//     * Test of GetParticleCoordinates
//     */
//    public void testGetPathStartToEnd() {
//    	String tmpStructure = "A[START]-B-C(J-K[1])-D(E)(F-G)-H[1]-I[END]";
//    	PSmiles tmpPSmiles = new PSmiles(tmpStructure);
//    	tmpPSmiles.getPathStartToEnd();
//    	
//    	tmpStructure = "A-B-C[START](J-K[1])-D(E(E)-F-G)-H[1][END]-I";
//    	tmpPSmiles = new PSmiles(tmpStructure);
//    	tmpPSmiles.getPathStartToEnd();
//    	
//    	tmpStructure = "A-B[END]-C(J-K[1])-D(E)(F-G)-H[1]-I[START]-L";
//    	tmpPSmiles = new PSmiles(tmpStructure);
//    	tmpPSmiles.getPathStartToEnd();
//    	
//    	tmpStructure = "A-A(B)-B[END]-C(J-K[1])-D(E)(F-G)-H[1]-I[START]-L(L)-M(N)-N";
//    	tmpPSmiles = new PSmiles(tmpStructure);
//    	tmpPSmiles.getPathStartToEnd();
//    	
//    	tmpStructure = "MetNH2(Met-HAcSC[START])-HAcPD1" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-Acetamide)-HAc" +
//        		"-MetNH2(Met(Met)(Met-Met))-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2-HAc" +
//        		"-ProRing-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-ProRing-HAc" +
//        		"-MetNH2(Met-Benzene)-HAc" +
//        		"-MetNH2(Met-Phenol)-HAc" +
//        		"-ProRing-HAc" +
//        		"-MetNH2(Met(Met)(Met-Met))-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(MetOH)-HAc" +
//        		"-MetNH2(Met)-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-MetNH2(Met-Met-Acetamide)-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Met-Met-Guanidine)-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2(Met-Phenol)-HAc" +
//        		"-MetNH2(Met-Met-MetSH)-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-Met-Met-Guanidine)-HAc" +
//        		"-MetNH2(Met-Phenol)-HAc" +
//        		"-MetNH2(Met)-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met)-HAc" +
//        		"-MetNH2(Met(Met)(Met-Met))-HAc" +
//        		"-MetNH2(Met)-HAc" +
//        		"-MetNH2(Met-Benzene)-HAc" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-MetNH2(Met-Acetamide)-HAc" +
//        		"-MetNH2(Met)-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-Phenol)-HAc" +
//        		"-MetNH2(MetOH)-HAc" +
//        		"-MetNH2(Met-Phenol)-HAc" +
//        		"-MetNH2(Met)-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-Phenol)-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2(MetOH)-HAc" +
//        		"-MetNH2(MetSH)-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2(Met)-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Met-Acetamide)-HAc" +
//        		"-MetNH2(Met-Acetamide)-HAc" +
//        		"-MetNH2(Met-Phenol)-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-HAcSC)-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met-Met-Met-Guanidine)-HAc" +
//        		"-MetNH2(Met(Met)(Met-Met))-HAc" +
//        		"-MetNH2(Met)-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(MetSH)-HAc" +
//        		"-MetNH2(MetOH)-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-Acetamide)-HAc" +
//        		"-MetNH2(MetSH)-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-Benzene)-HAc" +
//        		"-MetNH2(Met-Benzene)-HAc" +
//        		"-MetNH2(Met(Met)(Met-Met))-HAc" +
//        		"-ProRing-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met(Met)(Met-Met))-HAc" +
//        		"-MetNH2(Met)-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Benzene)-HAc" +
//        		"-MetNH2(Met(Met)(Met-Met))-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met)-HAc" +
//        		"-ProRing-HAc" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-MetNH2(Met-Acetamide)-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2(Met(Met)(Met-Met))-HAc" +
//        		"-MetNH2(Met-Phenol)-HAc" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Met-Met-Guanidine)-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Imidazole)-HAc" +
//        		"-MetNH2(MetOH)-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met(Met)(Met-Met))-HAc" +
//        		"-MetNH2(MetOH)-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-ProRing-HAc" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-MetNH2(Met(Met)(Met-Met))-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Benzene)-HAc" +
//        		"-MetNH2(MetOH)-HAc" +
//        		"-MetNH2(MetOH)-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met(Met)(Met-Met))-HAc" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Met-Acetamide)-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-MetNH2(Met(Met)(Met-Met))-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-MetNH2(Met(Met)(Met-Met))-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met(Met)(Met-Met))-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-HAcSC)-HAc" +
//        		"-MetNH2(MetOH)-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-Phenol)-HAc" +
//        		"-MetNH2(Met-Met-Met-Guanidine)-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met-Phenol)-HAc" +
//        		"-MetNH2(Met-Met-Acetamide)-HAc" +
//        		"-MetNH2(MetSH)-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-HAcSC)-HAc" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-MetNH2(Met-Benzene)-HAc" +
//        		"-MetNH2(Met(Met)(Met-Met))-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2(Met-Met-Met-Guanidine)-HAc" +
//        		"-MetNH2(Met-Acetamide)-HAc" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-ProRing-HAc" +
//        		"-ProRing-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met-Benzene)-HAc" +
//        		"-MetNH2(Met-Met-Acetamide)-HAc" +
//        		"-MetNH2(Met)-HAc" +
//        		"-MetNH2(MetOH)-HAc" +
//        		"-MetNH2(MetOH)-HAc" +
//        		"-MetNH2(Met-Benzene)-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-Met-Met-Guanidine)-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-Met-Acetamide)-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met)-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met(Met)(Met-Met))-HAc" +
//        		"-MetNH2(Met-Met-MetSH)-HAc" +
//        		"-MetNH2(Met-Acetamide)-HAc" +
//        		"-MetNH2(MetOH)-HAc" +
//        		"-MetNH2(MetOH)-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(MetOH)-HAc" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-ProRing-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAcPD2" +
//        		"-MetNH2(Met-Met-Acetamide)-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-MetNH2(Met-Imidazole)-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-Acetamide)-HAc" +
//        		"-MetNH2(Met(Met)(Met-Met))-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-MetNH2(Met-Met-Met-Guanidine)-HAc" +
//        		"-MetNH2(Met-Benzene)-HAc" +
//        		"-MetNH2(MetOH)-HAc" +
//        		"-MetNH2(Met-Imidazole)-HAc" +
//        		"-MetNH2(Met)-HAc" +
//        		"-MetNH2(Met-Met-Met-Guanidine)-HAc" +
//        		"-MetNH2(Met-HAcSC)-HAc" +
//        		"-ProRing-HAc" +
//        		"-MetNH2(Met(Met)(Met-Met))-HAc" +
//        		"-MetNH2(Met-Phenol)-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met-Acetamide)-HAc" +
//        		"-MetNH2(Met-Met-Acetamide)-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(MetOH)-HAc" +
//        		"-ProRing-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-MetNH2(Met)-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAcPD3" +
//        		"-ProRing-HAc" +
//        		"-MetNH2(Met-Benzene)-HAc" +
//        		"-MetNH2(Met-Imidazole)-HAc" +
//        		"-MetNH2(Met-Imidazole)-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met-Benzene)-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met-Met-MetSH)-HAc" +
//        		"-MetNH2(Met-Benzene)-HAc" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met-Phenol)-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met(Met)(Met-Met))-HAc" +
//        		"-MetNH2(MetSH)-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met-Benzene)-HAc" +
//        		"-MetNH2(Met-Met-Met-Guanidine)-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Met-MetSH)-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2(Met-Benzene)-HAc" +
//        		"-MetNH2(Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-MetNH2(Met-Benzene)-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Met-Acetamide)-HAc" +
//        		"-MetNH2(Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-Phenol)-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2(MetSH)-HAc" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-MetNH2(MetOH)-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met(Met)(Met-Met))-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-ProRing-HAc" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Benzene)-HAc" +
//        		"-MetNH2(Met)-HAc" +
//        		"-MetNH2(Met(Met)(Met-Met))-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Acetamide)-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2(MetOH)-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Acetamide)-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2(Met-Phenol)-HAc" +
//        		"-MetNH2(Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(MetOH)-HAc" +
//        		"-MetNH2(Met-Acetamide)-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2(Met(Met)(Met-Met))-HAc" +
//        		"-MetNH2(Met)-HAc" +
//        		"-MetNH2(MetOH)-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met)-HAc" +
//        		"-ProRing-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(MetOH)-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2(Met)-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met)-HAc" +
//        		"-MetNH2(Met-Met-Met-Guanidine)-HAc" +
//        		"-MetNH2(Met-Met-Met-Guanidine)-HAc" +
//        		"-MetNH2(Met-Benzene)-HAc" +
//        		"-MetNH2(Met-Acetamide)-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-ProRing-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Met-Met-Guanidine)-HAc" +
//        		"-MetNH2(Met-Met-Acetamide)-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met-Phenol)-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAcPD4" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-MetNH2(MetOH)-HAc" +
//        		"-MetNH2(Met)-HAc" +
//        		"-MetNH2(Met(Met)(Met-Met))-HAc" +
//        		"-MetNH2(Met(Met)(Met-Met))-HAc" +
//        		"-MetNH2(Met(Met)(Met-Met))-HAc" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-ProRing-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-ProRing-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met)-HAc" +
//        		"-MetNH2(MetOH)-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-ProRing-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Benzene)-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2(Met)-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met(Met)(Met-Met))-HAc" +
//        		"-MetNH2(Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-HAcSC)-HAc" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2(MetOH)-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2-HAc" +
//        		"-ProRing-HAc" +
//        		"-MetNH2(Met-Acetamide)-HAc" +
//        		"-MetNH2(Met-Met-Met-Guanidine)-HAc" +
//        		"-MetNH2(Met-Met-Met-Guanidine)-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(MetSH)-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2-HAc" +
//        		"-ProRing-HAc" +
//        		"-MetNH2(Met-Met-MetSH)-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Met-MetSH)-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met-Phenol)-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Acetamide)-HAc" +
//        		"-MetNH2(Met-Acetamide)-HAc" +
//        		"-ProRing-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2(Met)-HAc" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met(Met)(Met-Met))-HAc" +
//        		"-MetNH2(Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met-Pyrrole-Benzene)-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Imidazole)-HAc" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met-HAcSC)-HAc" +
//        		"-MetNH2(Met(Met)(Met-Met))-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met-Phenol)-HAc" +
//        		"-MetNH2(Met-Phenol)-HAc" +
//        		"-MetNH2(Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2(Met-Imidazole)-HAc" +
//        		"-MetNH2(Met-Benzene)-HAc" +
//        		"-MetNH2(Met-Benzene)-HAc" +
//        		"-MetNH2(Met(Met)(Met-Met))-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-Met-Met-Guanidine)-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2(MetOH)-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met(Met)(Met-Met))-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2(Met-Phenol)-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met-Phenol)-HAc" +
//        		"-MetNH2(Met-Met-Acetamide)-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-ProRing-HAc" +
//        		"-ProRing-HAc" +
//        		"-MetNH2(Met)-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2(MetOH)-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Met-Acetamide)-HAc" +
//        		"-MetNH2(Met-Imidazole)-HAc" +
//        		"-ProRing-HAc" +
//        		"-MetNH2(MetOH)-HAc" +
//        		"-MetNH2(Met(Met)(Met-Met))-HAc" +
//        		"-MetNH2(Met-Benzene)-HAc" +
//        		"-MetNH2(Met-HAcSC)-HAc" +
//        		"-MetNH2(Met)-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met)-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-ProRing-HAc" +
//        		"-MetNH2(Met-HAcSC)-HAc" +
//        		"-ProRing-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met)-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-ProRing-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met)-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2(MetOH)-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2(Met-Acetamide)-HAcPD5" +
//        		"-MetNH2(Met-Met-MetSH)-HAc" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Met-MetSH)-HAc" +
//        		"-MetNH2(Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-Phenol)-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met)-HAc" +
//        		"-MetNH2(MetOH)-HAc" +
//        		"-MetNH2(Met-Met-Acetamide)-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(MetOH)-HAc" +
//        		"-MetNH2(Met-Acetamide)-HAc" +
//        		"-MetNH2(Met)-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2(Met-Met-Met-Guanidine)-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Met-Met-Guanidine)-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Met-Met-Guanidine)-HAc" +
//        		"-MetNH2(Met-Benzene)-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-HAcSC)-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2(Met(Met)(Met))-HAc" +
//        		"-ProRing-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(MetOH-Phenol)-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2(Met(Met)(Met-Met))-HAc" +
//        		"-MetNH2(Met-HAcSC)-HAc" +
//        		"-MetNH2-HAc" +
//        		"-MetNH2(Met-Met-Met-Guanidine)-HAc" +
//        		"-MetNH2(Met)-HAc" +
//        		"-MetNH2(Met(Met)(Met-Met))-HAc" +
//        		"-MetNH2(Met-Met-Met-Guanidine)-HAc" +
//        		"-MetNH2(Met-Met-HAcSC)-HAc" +
//        		"-MetNH2(Met(Met)(Met-Met))-HAc" +
//        		"-MetNH2(Met-Met(Met)(Met))-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-MetNH2(Met-Met-Met-MetNH2)-HAc" +
//        		"-ProRing-HAc" +
//        		"-MetNH2(Met(Met)(Met[END]))-HAc";
//    	tmpPSmiles = new PSmiles(tmpStructure);
//    	tmpPSmiles.getPathStartToEnd();
//    	
//    }
//    
    // </editor-fold>
    // </editor-fold>
    //
    // <editor-fold defaultstate="collapsed" desc="Private methods">
    /**
     * Reads a text file into a single string
     *
     * @param aSourcePathname Full pathname of the text file to be read
     * @return The string representation of the content of the text file or null
     * if nothing could be read
     */
    private String readTextFileIntoSingleString(String aSourcePathname) {

        // <editor-fold defaultstate="collapsed" desc="Checks">
        if (aSourcePathname == null || aSourcePathname.isEmpty()) {
            return null;
        }
        if (!(new File(aSourcePathname)).isFile()) {
            return null;
        }

        // </editor-fold>
        BufferedReader tmpBufferedReader = null;
        try {
            FileReader tmpFileReader = new FileReader(aSourcePathname);
            tmpBufferedReader = new BufferedReader(tmpFileReader, 65536);
            StringBuilder tmpStringBuilder = new StringBuilder(65536);
            String tmpSeparator = System.getProperty("line.separator");
            String tmpLine;
            while ((tmpLine = tmpBufferedReader.readLine()) != null) {
                if (tmpStringBuilder.length() == 0) {
                    tmpStringBuilder.append(tmpLine);
                } else {
                    tmpStringBuilder.append(tmpSeparator);
                    tmpStringBuilder.append(tmpLine);
                }
            }
            return tmpStringBuilder.toString();
        } catch (IOException e) {
            return null;
        } finally {
            if (tmpBufferedReader != null) {
                try {
                    tmpBufferedReader.close();
                } catch (IOException e) {
                    return null;
                }
            }
        }
    }

    /*
     * Test of private method getNeightborParticle Please, set the method getNeightborParticle to public if you want to use this test method
     */
 /*
     * public void testGetNeighborParticle() { PSmiles tmpPSmiles = new PSmiles(null); List<int[]> tmpResult = tmpPSmiles.getNeighborParticles(false); assertNull("Test8.1", tmpResult);
     * 
     * tmpPSmiles = new PSmiles("A-B-C"); tmpResult = tmpPSmiles.getNeighborParticles(false); assertTrue("Test8.2", tmpResult.get(0)[0] == 0 && tmpResult.get(0)[1] == 1 && tmpResult.get(1)[0] == 1 &&
     * tmpResult.get(1)[1] == 2);
     * 
     * tmpPSmiles = new PSmiles("3A"); tmpResult = tmpPSmiles.getNeighborParticles(false); assertTrue("Test8.3", tmpResult.get(0)[0] == 0 && tmpResult.get(0)[1] == 1 && tmpResult.get(1)[0] == 1 &&
     * tmpResult.get(1)[1] == 2);
     * 
     * tmpPSmiles = new PSmiles("A-3(B-2(C-2(D)-2E))"); tmpResult = tmpPSmiles.getNeighborParticles(false); String tmpResultString = tmpPSmiles.getErrorMessage(); assertTrue("Test8.4.1",
     * tmpResultString == null); assertTrue("Test8.4.2", tmpResult.get(0)[0] == 0 && tmpResult.get(0)[1] == 1 && tmpResult.get(1)[0] == 0 && tmpResult.get(1)[1] == 2 && tmpResult.get(2)[0] == 0 &&
     * tmpResult.get(2)[1] == 3); assertTrue("Test8.4.3", tmpResult.get(3)[0] == 1 && tmpResult.get(3)[1] == 4 && tmpResult.get(4)[0] == 1 && tmpResult.get(4)[1] == 5 && tmpResult.get(5)[0] == 2 &&
     * tmpResult.get(5)[1] == 6 && tmpResult.get(6)[0] == 2 && tmpResult.get(6)[1] == 7 && tmpResult.get(7)[0] == 3 && tmpResult.get(7)[1] == 8 && tmpResult.get(8)[0] == 3 && tmpResult.get(8)[1] ==
     * 9); assertTrue("Test8.4.4", tmpResult.get(9)[0] == 4 && tmpResult.get(9)[1] == 10 && tmpResult.get(10)[0] == 4 && tmpResult.get(10)[1] == 11 && tmpResult.get(11)[0] == 4 && tmpResult.get(11)[1]
     * == 22 && tmpResult.get(12)[0] == 5 && tmpResult.get(12)[1] == 12 && tmpResult.get(13)[0] == 5 && tmpResult.get(13)[1] == 13 && tmpResult.get(14)[0] == 5 && tmpResult.get(14)[1] == 24 &&
     * tmpResult.get(15)[0] == 6 && tmpResult.get(15)[1] == 14 && tmpResult.get(16)[0] == 6 && tmpResult.get(16)[1] == 15 && tmpResult.get(17)[0] == 6 && tmpResult.get(17)[1] == 26 &&
     * tmpResult.get(18)[0] == 7 && tmpResult.get(18)[1] == 16 && tmpResult.get(19)[0] == 7 && tmpResult.get(19)[1] == 17 && tmpResult.get(20)[0] == 7 && tmpResult.get(20)[1] == 28 &&
     * tmpResult.get(21)[0] == 8 && tmpResult.get(21)[1] == 18 && tmpResult.get(22)[0] == 8 && tmpResult.get(22)[1] == 19 && tmpResult.get(23)[0] == 8 && tmpResult.get(23)[1] == 30 &&
     * tmpResult.get(24)[0] == 9 && tmpResult.get(24)[1] == 20 && tmpResult.get(25)[0] == 9 && tmpResult.get(25)[1] == 21 && tmpResult.get(26)[0] == 9 && tmpResult.get(26)[1] == 32);
     * assertTrue("Test8.4.5", tmpResult.get(27)[0] == 22 && tmpResult.get(27)[1] == 23 && tmpResult.get(28)[0] == 24 && tmpResult.get(28)[1] == 25 && tmpResult.get(29)[0] == 26 &&
     * tmpResult.get(29)[1] == 27 && tmpResult.get(30)[0] == 28 && tmpResult.get(30)[1] == 29 && tmpResult.get(31)[0] == 30 && tmpResult.get(31)[1] == 31 && tmpResult.get(32)[0] == 32 &&
     * tmpResult.get(32)[1] == 33 && tmpResult.size() == 33);
     * 
     * tmpPSmiles = new PSmiles("A-3B[1]-C[2]-D(E[2])-3F[1]"); tmpResult = tmpPSmiles.getNeighborParticles(false); tmpResultString = tmpPSmiles.getErrorMessage(); assertNull("Test8.5.1",
     * tmpResultString); assertTrue("Test8.5.2", tmpResult.get(0)[0] == 0 && tmpResult.get(0)[1] == 1 && tmpResult.get(1)[0] == 1 && tmpResult.get(1)[1] == 2 && tmpResult.get(2)[0] == 2 &&
     * tmpResult.get(2)[1] == 3 && tmpResult.get(3)[0] == 3 && tmpResult.get(3)[1] == 4 && tmpResult.get(4)[0] == 3 && tmpResult.get(4)[1] == 9 && tmpResult.get(5)[0] == 4 && tmpResult.get(5)[1] == 5
     * && tmpResult.get(6)[0] == 4 && tmpResult.get(6)[1] == 6 && tmpResult.get(7)[0] == 5 && tmpResult.get(7)[1] == 6 && tmpResult.get(8)[0] == 5 && tmpResult.get(8)[1] == 7 && tmpResult.get(9)[0] ==
     * 7 && tmpResult.get(9)[1] == 8 && tmpResult.get(10)[0] == 8 && tmpResult.get(10)[1] == 9 && tmpResult.size() == 11);
     * 
     * tmpPSmiles = new PSmiles("3{A-B[HEAD]-2C-D[TAIL]}"); tmpResult = tmpPSmiles.getNeighborParticles(false); assertTrue("Test8.6", tmpResult.get(0)[0] == 0 && tmpResult.get(0)[1] == 3 &&
     * tmpResult.get(1)[0] == 1 && tmpResult.get(1)[1] == 4 && tmpResult.get(2)[0] == 2 && tmpResult.get(2)[1] == 5 && tmpResult.get(3)[0] == 3 && tmpResult.get(3)[1] == 6 && tmpResult.get(4)[0] == 4
     * && tmpResult.get(4)[1] == 8 && tmpResult.get(5)[0] == 5 && tmpResult.get(5)[1] == 10 && tmpResult.get(6)[0] == 6 && tmpResult.get(6)[1] == 7 && tmpResult.get(7)[0] == 7 && tmpResult.get(7)[1]
     * == 12 && tmpResult.get(8)[0] == 8 && tmpResult.get(8)[1] == 9 && tmpResult.get(9)[0] == 9 && tmpResult.get(9)[1] == 13 && tmpResult.get(10)[0] == 10 && tmpResult.get(10)[1] == 11 &&
     * tmpResult.get(11)[0] == 11 && tmpResult.get(11)[1] == 14 && tmpResult.get(12)[0] == 12 && tmpResult.get(12)[1] == 4 && tmpResult.get(13)[0] == 13 && tmpResult.get(13)[1] == 5 &&
     * tmpResult.size() == 14);
     * 
     * tmpPSmiles = new PSmiles("A-2{B[TAIL]-C[HEAD]}-D"); tmpResult = tmpPSmiles.getNeighborParticles(false); assertTrue("Test8.7", tmpResult.get(0)[0] == 0 && tmpResult.get(0)[1] == 3 &&
     * tmpResult.get(1)[0] == 1 && tmpResult.get(1)[1] == 4 && tmpResult.get(2)[0] == 1 && tmpResult.get(2)[1] == 3 && tmpResult.get(3)[0] == 2 && tmpResult.get(3)[1] == 5 && tmpResult.get(4)[0] == 2
     * && tmpResult.get(4)[1] == 4 && tmpResult.size() == 5);
     * 
     * tmpPSmiles = new PSmiles("A-2{B-C[HEAD]-3D-E[TAIL]}"); tmpResult = tmpPSmiles.getNeighborParticles(false); assertTrue("Test8.8", tmpResult.get(0)[0] == 0 && tmpResult.get(0)[1] == 3 &&
     * tmpResult.get(1)[0] == 1 && tmpResult.get(1)[1] == 3 && tmpResult.get(2)[0] == 2 && tmpResult.get(2)[1] == 4 && tmpResult.get(3)[0] == 3 && tmpResult.get(3)[1] == 5 && tmpResult.get(4)[0] == 4
     * && tmpResult.get(4)[1] == 8 && tmpResult.get(5)[0] == 5 && tmpResult.get(5)[1] == 6 && tmpResult.get(6)[0] == 6 && tmpResult.get(6)[1] == 7 && tmpResult.get(7)[0] == 7 && tmpResult.get(7)[1] ==
     * 11 && tmpResult.get(8)[0] == 8 && tmpResult.get(8)[1] == 9 && tmpResult.get(9)[0] == 9 && tmpResult.get(9)[1] == 10 && tmpResult.get(10)[0] == 10 && tmpResult.get(10)[1] == 12 &&
     * tmpResult.get(11)[0] == 11 && tmpResult.get(11)[1] == 4 && tmpResult.size() == 12);
     * 
     * tmpPSmiles = new PSmiles("2{3A[HEAD]-B-C[TAIL]}"); tmpResult = tmpPSmiles.getNeighborParticles(false); assertTrue("Test8.9", tmpResult.get(0)[0] == 0 && tmpResult.get(0)[1] == 1 &&
     * tmpResult.get(1)[0] == 1 && tmpResult.get(1)[1] == 2 && tmpResult.get(2)[0] == 2 && tmpResult.get(2)[1] == 6 && tmpResult.get(3)[0] == 3 && tmpResult.get(3)[1] == 4 && tmpResult.get(4)[0] == 4
     * && tmpResult.get(4)[1] == 5 && tmpResult.get(5)[0] == 5 && tmpResult.get(5)[1] == 7 && tmpResult.get(6)[0] == 6 && tmpResult.get(6)[1] == 8 && tmpResult.get(7)[0] == 7 && tmpResult.get(7)[1] ==
     * 9 && tmpResult.get(8)[0] == 8 && tmpResult.get(8)[1] == 5 && tmpResult.size() == 9);
     * 
     * tmpPSmiles = new PSmiles("A(B-C-D)(E-E)(F)-D"); tmpResult = tmpPSmiles.getNeighborParticles(false); assertTrue("Test8.10", tmpResult.get(0)[0] == 0 && tmpResult.get(0)[1] == 1 &&
     * tmpResult.get(1)[0] == 0 && tmpResult.get(1)[1] == 4 && tmpResult.get(2)[0] == 0 && tmpResult.get(2)[1] == 6 && tmpResult.get(3)[0] == 0 && tmpResult.get(3)[1] == 7 && tmpResult.get(4)[0] == 1
     * && tmpResult.get(4)[1] == 2 && tmpResult.get(5)[0] == 2 && tmpResult.get(5)[1] == 3 && tmpResult.get(6)[0] == 4 && tmpResult.get(6)[1] == 5 && tmpResult.size() == 7); }
     */
    // </editor-fold>
    
}
