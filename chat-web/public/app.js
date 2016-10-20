

// Configure loading modules from the lib directory,
// except for 'app' ones, which are in a sibling
// directory.
requirejs.config({
    baseUrl: 'javascripts',
});

// Start loading the main app file. Put all of
// your application logic in there.
requirejs(['global']);