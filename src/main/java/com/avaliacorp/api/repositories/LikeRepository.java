package com.avaliacorp.api.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import com.avaliacorp.api.models.LikeModel;

@Repository
public class LikeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private class LikeRowMapper implements RowMapper<LikeModel> {
        @Override
        @Nullable
        public LikeModel mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            LikeModel like = new LikeModel();
            like.setCommentId(rs.getInt("comentario_id"));
            like.setPostId(rs.getInt("avaliacao_id"));
            like.setUserId(rs.getString("usuario_id"));
            return like;
        }
    }

    /**
     * Cria uma nova entidade like
     * @param entity A entidade a ser criada (verifique antes se as chaves estão corretas)
     * @return A entidade criada
     */
    public LikeModel save(LikeModel entity){
        jdbcTemplate.update("INSERT INTO likes(usuario_id, avaliacao_id, comentario_id) VALUES (?, ?, ?)", entity.getUserId(), entity.getPostId(), entity.getCommentId());

        return findUnique(entity);
    }

    /**
     * Procura um like
     * @param entity A entidade a ser procurada, espera-se que passe todas as chaves que compoem o like
     * @return A entidade like encontrada
     */
    public LikeModel findUnique(LikeModel entity){
        String SQL_SELECT_QUERY;
        if(entity.getPostId() == null){
            SQL_SELECT_QUERY = String.format("SELECT * FROM likes WHERE (usuario_id, comentario_id) = ('%s',%d)", entity.getUserId(), entity.getCommentId());
        }
        else {
            SQL_SELECT_QUERY = String.format("SELECT * FROM likes WHERE (usuario_id, avaliacao_id) = ('%s',%d)", entity.getUserId(), entity.getPostId());
        }
        List<LikeModel> like = jdbcTemplate.query(SQL_SELECT_QUERY, new LikeRowMapper());
        if(like.isEmpty()) return null;
        return like.get(0);
    }

    /**
     * Conta a quantidade de linhas da tabela likes
     * @return Número de linhas de todas as tabelas
     */
    public Integer count(){
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM likes", Integer.class);
    }

    /**
     * Conta a quantidade de linhas na tabela likes que contém a coluna 'avaliacao_id' igual ao parâmetro 'postId'
     * @param postId Id de um post
     * @return Número de likes do post
     */
    public Integer countByPost(Integer postId){
        String SQL_SELECT_QUERY = String.format("SELECT COUNT(*) FROM likes WHERE avaliacao_id = %d", postId);
        return jdbcTemplate.queryForObject(SQL_SELECT_QUERY, Integer.class);
    }

    /**
     * Conta a quantidade de linha na tabela likes que contém a coluna 'comentario_id' igual ao parâmetro 'commentId'
     * @param commentId Id de um comment
     * @return Número de likes do comment
     */
    public Integer countByComment(Integer commentId){
        String SQL_SELECT_QUERY = String.format("SELECT COUNT(*) FROM likes WHERE comentario_id = %d", commentId);
        return jdbcTemplate.queryForObject(SQL_SELECT_QUERY, Integer.class);
    }

    /**
     * Deleta um like do banco de dados
     * @param entity Uma entidade que deve conter todas as chaves que compoem o like
     */
    public void delete(LikeModel entity){
        String SQL_DELETE_QUERY;
        if(entity.getPostId() == null){
            SQL_DELETE_QUERY = String.format("DELETE FROM likes WHERE (usuario_id, comentario_id) = (?,%d)", entity.getCommentId());
        }
        else {
            SQL_DELETE_QUERY = String.format("DELETE FROM likes WHERE (usuario_id, avaliacao_id) = (?,%d)", entity.getPostId());
        }
        jdbcTemplate.update(SQL_DELETE_QUERY, entity.getUserId());
    }

}
