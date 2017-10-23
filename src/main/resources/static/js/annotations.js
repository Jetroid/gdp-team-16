window.onload = function() {
    var annotations = [];
    var selectionText = "";
    var selectionStartOffset = 0;
    var selectionEndOffset = 0;

    // Rangy
    rangy.init();
    // highlighting
    highlighter = rangy.createHighlighter(document, "TextRange");
    highlighter.addClassApplier(rangy.createClassApplier("annotation", {
        ignoreWhiteSpace: true,
        elementTagName: "a",
        elementProperties: {
            onclick: function() {
                annotationSelection(this);
            }
        }
    }));

    // annotation link clicked (probably wont need this)
    function annotationSelection(elem) {
        //var highlight = highlighter.getHighlightForElement(elem);
    }

    function handleTextSelection(e) {
        var selection = rangy.getSelection();
        var text = selection.toString();

        // selection is empty
        if (text === "") {
            return;
        }

        // clear existing annotation buttons
        $('.create-annotation-ui').remove();

        if (selection.rangeCount > 0) {

            var range = selection.getRangeAt(0);

            // retrieves the selection start and end indexes relative to the start of visible text within the "case" node
            var offsets = range.toCharacterRange(document.getElementById('case'));

            // selection hasn't changed (so don't respawn button)
            if (selectionStartOffset == offsets.start && selectionEndOffset == offsets.end) {
                return;
            }

            // the text and start/end indexes of the selected text
            selectionText = text;
            selectionStartOffset = offsets.start;
            selectionEndOffset = offsets.end;

            // check an identical annotation does not already exist
            if (annotationExists(selectionStartOffset, selectionEndOffset)) {
                return;
            }

            // spawn an annotation creation button near the user's cursor
            var div = $('<div class="create-annotation-ui">')
                .css({
                    //TODO: Bounds checking so that button always spawns within the page
                    "left": (e.pageX + 45) + 'px',
                    "top": (e.pageY + 15) + 'px'
                })
                .append($('<button type="button" id="create_annotation_button">Create Annotation</button>'))
                .appendTo(document.body);
            $('#create_annotation_button').click(createAnnotation);
        }
    }

    function createAnnotation() {
        var text = selectionText;
        var start = selectionStartOffset;
        var end = selectionEndOffset;

        // check an identical annotation does not already exist
        if (annotationExists(start, end)) {
            return;
        }

        // create the annotation and push to array
        // TODO: Unique ID's (involves backend)
        var annotation = {start: start, end: end, text: text, id: 0};
        annotations.push(annotation);

        // clear existing annotation buttons
        $('.create-annotation-ui').remove();

        // update styling
        updateAnnotations();
    }

    // adds styling and links to all annotations
    function updateAnnotations() {
        // clear existing highlights
        highlighter.removeAllHighlights();

        /* *old method*
            // create a new range encompassing the annotation text and apply it to the selection
            var selection = rangy.getSelection();
            var range = rangy.createRange();
            range.selectCharacters(document.getElementById('case'), ann.start, ann.end);
            selection.setSingleRange(range);

            // apply the "annotation" class
            highlighter.highlightSelection("annotation", {exclusive: true, containerElementId: "case"});
        */

        // convert all annotations into CharacterRange objects
        for (var i = 0; i < annotations.length; i++) {
            var ann = annotations[i];
            // create a new range encompassing the annotation text and apply it to the selection
            var range = rangy.createRange();
            range.selectCharacters(document.getElementById('case'), ann.start, ann.end);

            // from the range we can convert to a CharacterRange
            var converter = highlighter.converter;
            var charRange =  converter.rangeToCharacterRange(range, document.getElementById('case'));

            // each annotation is applied one at a time so the correct annotation link can be manually inserted
            var charRanges = [];
            charRanges.push(charRange);
            highlighter.highlightCharacterRanges("annotation", charRanges);

            // add links manually to the highlights
            var highlights = document.getElementsByClassName("annotation");
            for(var j = 0; j < highlights.length; j++) {
                // so annotations with links aren't overwritten
                if(highlights[j].href !== "") {
                    continue;
                }
                // set the link to be the annotations discussion page with an anchor pointing to the annotation
                highlights[j].href = "annotations#" + ann.start + "-" + ann.end;
            }
        }
    }

    // returns true if there exists an annotation with the identical start and end indexes
    function annotationExists(start, end) {
        // check an identical annotation does not already exist
        for (var i = 0; i < annotations.length; i++) {
            // annotation already exists
            if (annotations[i].start === start && annotations[i].end === end) {
                return true;
            }
        }
        return false;
    }

    // on releasing the mouse check for selected text
    $(document).click(function (e) {
        handleTextSelection(e);
    });
};