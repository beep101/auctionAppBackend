const { merge } = require('webpack-merge');
const common = require('./webpack.common.js');

module.exports = merge(common, {
    devtool: 'sourcemaps',
    cache: true,
    mode: 'development',    
});