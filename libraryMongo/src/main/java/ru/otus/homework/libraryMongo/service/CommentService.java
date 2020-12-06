package ru.otus.homework.libraryMongo.service;

public interface CommentService {

    String saveComment(String bookIdString, String name);

    String updateComment(String commentIdString, String bookIdString, String name);

    String deleteComment(String commentIdString);

    String getComment(String id);

    String getAllComments();

    String getCommentsByBookId(String id);

    long getCommentsCount();
}