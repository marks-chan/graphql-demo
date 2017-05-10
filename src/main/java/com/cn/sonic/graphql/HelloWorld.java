package com.cn.sonic.graphql;

import java.util.Map;

import graphql.GraphQL;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;

import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;

/**
 * 功能描述：
 *
 * @author <a href="mailto:wanjie.cwj@alibaba-inc.com">陈玩杰</a>
 * @version 1.0.0
 * @create on: 2017/5/6
 */
public class HelloWorld {

    public static void main(String[] args) {
        GraphQLObjectType queryType = newObject()
            .name("helloWorldQuery")
            .field(newFieldDefinition()
                .type(GraphQLString)
                .name("hello")
                .staticValue("world"))
            .build();

        GraphQLSchema schema = GraphQLSchema.newSchema()
            .query(queryType)
            .build();

        GraphQL graphQL = GraphQL.newGraphQL(schema).build();

        Map<String, Object> result = graphQL.execute("{hello}").getData();
        System.out.println(result);
        // Prints: {hello=world}
    }
}
