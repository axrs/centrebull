'use strict';
require('events').EventEmitter.prototype._maxListeners = 100;

var gulp = require('gulp');
var watch = require('gulp-watch-less');
var less = require('gulp-less');

gulp.task('default', function () {
    return gulp.src('src/less/bare.less')
        .pipe(watch('src/less/*.less'))
        .pipe(less())
        .pipe(gulp.dest('resources/public/css'))
});
