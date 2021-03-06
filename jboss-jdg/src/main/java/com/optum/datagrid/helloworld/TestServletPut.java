package com.optum.datagrid.helloworld;

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.infinispan.Cache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.RemoteCache;


import java.util.logging.Logger;

/**
 * A simple servlet storing value for key "hello" into the cache.
 * 
 * @author mupadras
 * 
 */
@SuppressWarnings("serial")
@WebServlet("/TestServletPut")
public class TestServletPut extends HttpServlet {

    private static final String PAGE_HEADER = "<html><head /><body>";

    private static final String PAGE_FOOTER = "</body></html>";

    @Inject
    private Logger log;

    @Inject
    RemoteCacheManager m;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        log.info("putting hello-world");
        RemoteCache<String, String> c = m.getCache();
        c.put("hello", "world");

        PrintWriter writer = resp.getWriter();
        writer.println(PAGE_HEADER);
        writer.println("<h1>" + "Put Infinispan: " + c.get("hello") + "</h1>");
        writer.println(PAGE_FOOTER);
        writer.close();
    }

}
