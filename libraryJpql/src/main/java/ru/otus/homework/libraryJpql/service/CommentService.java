package ru.otus.homework.libraryJpql.service;

public interface CommentService {

    String saveComment(String bookIdString, String name);

    String updateComment(String commentIdString, String bookIdString, String name);

    String getComment(String id);

    String getAllComments();

    String getCommentsByBookId(String id);

    String getCommentsCount();
}