package com.godaddy.sonar.ruby.metricfu.rules;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.*;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


/**
 * Created by akash.v on 06/05/16.
 */
public class InMemoryRuleStore {

    private Directory ramDirectory;
    private StandardAnalyzer analyzer;
    private IndexWriterConfig config;
    private IndexWriter indexWriter;

    public InMemoryRuleStore(){
        analyzer = new StandardAnalyzer(Version.LUCENE_42);
        config = new IndexWriterConfig(Version.LUCENE_42, analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);

        ramDirectory = new RAMDirectory();

    }

    public void addRule(String ruleId, String discription){
        try {
            indexWriter = new IndexWriter(ramDirectory, config);
            FieldType type = new FieldType();
            type.setIndexed(true);
            type.setStored(true);
            type.setStoreTermVectors(true);
            Document doc = new Document();
            doc.add(new StringField("ruleId", ruleId, Store.YES));
            doc.add(new Field("description", discription, type));
            indexWriter.addDocument(doc);
            indexWriter.commit();
            indexWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String findRule(String searchForSimilar) {
        IndexReader reader = null;
        try {
            reader = DirectoryReader.open(ramDirectory);
            IndexSearcher indexSearcher = new IndexSearcher(reader);

            MoreLikeThis mlt = new MoreLikeThis(reader);
            mlt.setAnalyzer(analyzer);
            mlt.setFieldNames(new String[]{"description", "ruleId"});
            mlt.setMinTermFreq(0);
            mlt.setMinDocFreq(0);

            Reader sReader = new StringReader(searchForSimilar);
            Query query = mlt.like(sReader, null);
            TopDocs topDocs = indexSearcher.search(query, 1);

            if(topDocs.scoreDocs.length > 0)
                return indexSearcher.doc(topDocs.scoreDocs[0].doc).get("ruleId");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}

