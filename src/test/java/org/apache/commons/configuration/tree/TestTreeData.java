/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.configuration.tree;

/**
 * Test class for {@code TreeData}
 *
 * @version $Id$
 */
public class TestTreeData extends AbstractImmutableNodeHandlerTest
{
    /**
     * {@inheritDoc} This implementation creates a TreeData object initialized
     * with the given root node.
     */
    @Override
    protected NodeHandler<ImmutableNode> createHandler(ImmutableNode root)
    {
        InMemoryNodeModel model = new InMemoryNodeModel(root);
        return model.getTreeData();
    }
}