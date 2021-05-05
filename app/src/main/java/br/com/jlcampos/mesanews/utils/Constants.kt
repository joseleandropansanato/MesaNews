package br.com.jlcampos.mesanews.utils

class Constants private constructor(){
    companion object{

        const val IDENTIFICATION = "MesaNews"

        const val URL = "https://mesa-news-api.herokuapp.com"

        const val AUTHORIZATION = "Authorization"
        const val BEARER = "Bearer "
        const val TOKEN_TESTE = "${BEARER}eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MjU5LCJlbWFpbCI6ImRpbWFzLmdhYnJpZWxAenJvYmFuay5jb20uYnIifQ.a3j7sRx8FIedZCfDGLocduOYpcibfIenX7TVJjv6Sis"

        const val QUERY_PAGE_SIZE = 20

        /** /v1/client/auth/signin **/
        const val signin_ws = "/v1/client/auth/signin"
        const val signin_email = "email"
        const val signin_password = "password"
        const val signin_code = "code"
        const val signin_message = "message"
        const val signin_token = "token"

        /** /v1/client/auth/signup **/
        const val signup_ws = "$URL/v1/client/auth/signup"
        const val signup_name = "name"
        const val signup_email = "email"
        const val signup_password = "password"
        const val signup_code = "code"
        const val signup_message = "message"
        const val signup_token = "token"

        /** /v1/client/news **/
        const val news_ws = "/v1/client/news"
        const val news_current_page = "current_page"
        const val news_per_page = "per_page"
        const val news_published_at = "published_at"
        const val news_pagination = "pagination"
        const val news_pagination_current_page = "current_page"
        const val news_pagination_per_page = "per_page"
        const val news_pagination_total_pages = "total_pages"
        const val news_pagination_total_items = "total_items"
        const val news_data = "data"
        const val news_data_title = "title"
        const val news_data_description = "description"
        const val news_data_content = "content"
        const val news_data_author = "author"
        const val news_data_published_at = "published_at"
        const val news_data_highlight = "highlight"
        const val news_data_url = "url"
        const val news_data_image_url = "image_url"

        /** /v1/client/news/highlights **/
        const val hl_ws = "/v1/client/news/highlights"
        const val hl_data = "data"
    }
}