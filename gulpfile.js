'use strict';
require('events').EventEmitter.prototype._maxListeners = 100;

var gulp = require('gulp');
var gutil = require('gulp-util');
var notify = require('gulp-notify');
var less = require('gulp-less');
var autoprefix = require('gulp-autoprefixer');
var minifyCSS = require('gulp-minify-css');

var lessDir = 'src/less';
var targetCSSDir = 'resources/public/css';

gulp.task('css', function () {
    return gulp.src(lessDir + '/bare.less')
        .pipe(less({ style: 'compressed' }).on('error', gutil.log))
        .pipe(gulp.dest(targetCSSDir))
        .pipe(notify('CSS minified'))
});

gulp.task('watch', function () {
    gulp.watch(lessDir + '/*.less', [ 'css' ]);
});

gulp.task('default', [ 'css', 'watch' ]);
gulp.task('build', [ 'css' ]);
