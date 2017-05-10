package com.cn.sonic.graphql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import graphql.GraphQL;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLOutputType;
import graphql.schema.GraphQLSchema;

import static graphql.Scalars.GraphQLInt;
import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;

/**
 * 功能描述：
 *
 * @author <a href="mailto:wanjie.cwj@alibaba-inc.com">陈玩杰</a>
 * @version 1.0.0
 * @create on: 2017/5/6
 */
public class GraphSchema {

    private GraphQLOutputType userType;

    private GraphQLSchema schema;

    public GraphSchema() {
        initOutputType();
        schema = GraphQLSchema.newSchema().query(newObject()
            .name("GraphQuery")
            .field(createUsersField())
            .field(createUserField())
            .build()).build();
    }

    public static void main(String[] args) {
        GraphQLSchema schema = new GraphSchema().getSchema();

        String query1 = "{users(page:2,size:5,name:\"john\") {id,sex,name,pic}}";
        String query2 = "{user(id:1) {id,sex,name}}";
        String query3 = "{user(id:6) {id,sex,name,pic},users(page:2,size:5,name:\"john\") {id,sex,name,pic}}";

        Map<String, Object> result1 = (Map<String, Object>)GraphQL.newGraphQL(schema).build().execute(query1).getData();
        Map<String, Object> result2 = (Map<String, Object>)GraphQL.newGraphQL(schema).build().execute(query2).getData();
        Map<String, Object> result3 = (Map<String, Object>)GraphQL.newGraphQL(schema).build().execute(query3).getData();

        Boolean resultFormat = false;
        // 查询用户列表
        System.out.println(JSON.toJSONString(result1, resultFormat));
        // 查询单个用户
        System.out.println(JSON.toJSONString(result2, resultFormat));
        // 单个用户、跟用户列表一起查
        System.out.println(JSON.toJSONString(result3, resultFormat));

    }

    public GraphQLSchema getSchema() {
        return schema;
    }

    public void initOutputType() {
        /**
         * 会员对象结构
         */
        userType = newObject()
            .name("User")
            .field(newFieldDefinition().name("id").type(GraphQLInt).build())
            .field(newFieldDefinition().name("age").type(GraphQLInt).build())
            .field(newFieldDefinition().name("sex").type(GraphQLInt).build())
            .field(newFieldDefinition().name("name").type(GraphQLString).build())
            .field(newFieldDefinition().name("pic").type(GraphQLString).build())
            .build();
    }

    /**
     * 查询单个用户信息
     *
     * @return
     */
    private GraphQLFieldDefinition createUserField() {
        return GraphQLFieldDefinition.newFieldDefinition()
            .name("user")
            .argument(newArgument().name("id").type(GraphQLInt).build())
            .type(userType)
            .dataFetcher(environment -> {
                // 获取查询参数
                int id = environment.getArgument("id");

                // 执行查询, 这里随便用一些测试数据来说明问题
                User user = new User();
                user.setId(id);
                user.setAge(id + 15);
                user.setSex(id % 2);
                user.setName("Name_" + id);
                user.setPic("pic_" + id + ".jpg");

                return user;
            })
            .build();
    }

    /**
     * 查询多个会员信息
     *
     * @return
     */
    private GraphQLFieldDefinition createUsersField() {
        return GraphQLFieldDefinition.newFieldDefinition()
            .name("users")
            .argument(newArgument().name("page").type(GraphQLInt).build())
            .argument(newArgument().name("size").type(GraphQLInt).build())
            .argument(newArgument().name("name").type(GraphQLString).build())
            .type(new GraphQLList(userType))
            .dataFetcher(environment -> {
                // 获取查询参数
                int page = environment.getArgument("page");
                int size = environment.getArgument("size");
                String name = environment.getArgument("name");

                // 执行查询, 这里随便用一些测试数据来说明问题
                List<User> list = new ArrayList<>(size);
                for (int i = 0; i < size; i++) {
                    User user = new User();
                    user.setId(i);
                    user.setAge(i + 15);
                    user.setSex(i % 2);
                    user.setName(name + "_" + page + "_" + i);
                    user.setPic("pic_" + i + ".jpg");
                    list.add(user);
                }
                return list;
            })
            .build();
    }

    public class User {
        private int id;
        private int age;
        private int sex;
        private String name;
        private String pic;
        // getter, setter...

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}


