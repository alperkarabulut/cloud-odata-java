/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.sap.core.odata.core.edm.provider;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.edm.provider.EntityContainerInfo;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.testutil.fit.BaseTest;

public class EdmImplProvTest extends BaseTest {

  private static EdmImplProv edm;

  @BeforeClass
  public static void getEdmImpl() throws Exception {
    EdmProvider edmProvider = mock(EdmProvider.class);

    EntityType entityType = new EntityType().setName("EntityType1");
    when(edmProvider.getEntityType(new FullQualifiedName("EntityType1Ns", "EntityType1"))).thenReturn(entityType);

    ComplexType complexType = new ComplexType().setName("ComplexType1");
    when(edmProvider.getComplexType(new FullQualifiedName("ComplexType1Ns", "ComplexType1"))).thenReturn(complexType);

    Association association = new Association().setName("Association1");
    when(edmProvider.getAssociation(new FullQualifiedName("Association1Ns", "Association1"))).thenReturn(association);

    EntityContainerInfo defaultEntityContainer = new EntityContainerInfo().setName("Container1");
    when(edmProvider.getEntityContainerInfo(null)).thenReturn(defaultEntityContainer);
    when(edmProvider.getEntityContainerInfo("Container1")).thenReturn(defaultEntityContainer);

    edm = new EdmImplProv(edmProvider);
  }

  @Test
  public void testEntityType() throws EdmException {
    assertEquals("EntityType1", edm.getEntityType("EntityType1Ns", "EntityType1").getName());
  }

  @Test
  public void testComplexType() throws EdmException {
    assertEquals("ComplexType1", edm.getComplexType("ComplexType1Ns", "ComplexType1").getName());
  }

  @Test
  public void testAssociation() throws EdmException {
    assertEquals("Association1", edm.getAssociation("Association1Ns", "Association1").getName());
  }

  @Test
  public void testDefaultEntityContainer() throws EdmException {
    assertEquals(edm.getEntityContainer("Container1"), edm.getDefaultEntityContainer());
  }
}
