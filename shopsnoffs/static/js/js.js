
function scroll() {
    if ($(window).scrollTop() > 0) {
        $('.navbar-default').addClass('navbar-fixed');
    } else {
        $('.navbar-default').removeClass('navbar-fixed');
    }
}

$(document).ready(function(){

    $('.t').tooltip();

    $(window).scroll(function(){
        scroll();
    });
    scroll();

    $('.list-group-item').click(function(e){
        if (e.target.nodeName == 'A' || e.target.nodeName == 'a') {
            if ($(this).find('input').prop('checked') == true) {
                $(this).removeClass('active');
                $(this).find('input').prop('checked', false);
            } else {
                $(this).addClass('active');
                $(this).find('input').prop('checked', true);
            } 
        } else {
            if ($(this).find('input').prop('checked') == true) {
                $(this).addClass('active');
            } else {
                $(this).removeClass('active');
            }
        }
        
    })

    $('.review-intro-item-head').click(function(){
        if ($(this).closest('.review-intro-item-wrapper').find('.review-intro-item-content').length) {
            $(this).closest('.review-intro-item-wrapper').find('.review-intro-item-content').fadeToggle();
            return false;
        }
    });

    $('.movie-trailer a').click(function() {
        $(this).hide();
        var iframe = $(this).closest('.movie-trailer').find('iframe');
        iframe.attr('src', iframe.data('src'));
        $(this).closest('.movie-trailer').find('.move-trailer-iframe').fadeIn();
        return false;
    });

    /*
    $('.movie-more').click(function(){
        $(this).next().fadeToggle();
        $(this).find('i').toggleClass('fa-arrow-circle-down');
        $(this).find('i').toggleClass('fa-arrow-circle-up');

        return false;
    });
    */

    $('.movie-similar').click(function(){
        $('.similar-movie-list').toggle();
        return false;
    });

    $('.star-rating').on('change', function () {
        $(this).next().text($(this).val());
    });


    $('.star-rating').rating({
     
    });



});