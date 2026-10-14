const newman = require('newman');

newman.run({
    collection: require('./reclamoscollectionPostman.json'),
    reporters: ['cli', 'html'],
    reporter: {
        html: {
            export: './reporte-resultados.html' // Nombre del archivo HTML de salida
        }
    }
}, function (err) {
    if (err) { throw err; }
    console.log('¡Pruebas finalizadas! Reporte HTML generado con éxito.');
});