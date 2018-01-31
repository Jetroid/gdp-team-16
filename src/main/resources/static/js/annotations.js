window.onload = function() {
    //var quotations = []; // now initialised in welcome.html
    var selectionText = "";
    var selectionStartOffset = 0;
    var selectionEndOffset = 0;

    // Rangy
    rangy.init();
    // highlighting
    highlighter = rangy.createHighlighter(document.getElementById("case"), "TextRange");
    highlighter.addClassApplier(rangy.createClassApplier("quotation", {
        ignoreWhiteSpace: true,
        elementTagName: "a",
        elementProperties: {
            onclick: function() {
                quotationSelection(this);
            }
        }
    }));

    $('#tabs a').on('click', function(e)  {
        var currentAttrValue = jQuery(this).attr('href');

        // Show/Hide Tabs
        $('#tab-container ' + currentAttrValue).show().siblings().hide();

        // Change/remove current tab to active
        $(this).parent('li').addClass('active').siblings().removeClass('active');

        e.preventDefault();
    });

    // quotation link clicked (probably wont need this)
    function quotationSelection(elem) {
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

            console.log(offsets.start + ", " + offsets.end);

            // the text and start/end indexes of the selected text
            selectionText = text;
            selectionStartOffset = offsets.start;
            selectionEndOffset = offsets.end;

            // check an identical quotation does not already exist
            if (quotationExists(selectionStartOffset, selectionEndOffset)) {
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
            $('#create_annotation_button').click(createQuotation);
        }
    }

    function createQuotation() {
        //TODO: CaseID hardcoded right now!
        var caseId = 1;
        var text = selectionText;
        var start = selectionStartOffset;
        var end = selectionEndOffset;

        console.log("creating quotation with start: " + start + " end: " + end);

        // check an identical quotation does not already exist
        if (quotationExists(start, end)) {
            return;
        }

        // create the quotation and push to array
        var quotation = {
            "caseId": caseId,
            "startIndex": start,
            "endIndex": end,
            "quote": text,
            "id": 0
        };
        quotations.push(quotation);

        //Send our quotation to the server
        var xhr = new XMLHttpRequest();
        xhr.open("POST", '/putquotation', true);
        //Send the proper header information along with the request
        xhr.setRequestHeader("Content-type", "application/json");
        xhr.onreadystatechange = function() {//Call a function when the state changes.
            if(xhr.readyState == XMLHttpRequest.DONE && xhr.status == 200) {
                createAnnotation(parseInt(xhr.response), text);
            }
        }
        xhr.send(JSON.stringify(quotation));

        // clear existing annotation buttons
        $('.create-annotation-ui').remove();

        // update styling
        updateStyling();
    }

    function createAnnotation(quotationId, quoteText){
        //TODO: authorId and caseId are hardcoded!
        var authorId = 1;
        var caseId = 1;

        var annotationEditor = document.createElement("div");
        annotationEditor.className = "annotation-container";
        annotationEditor.id = "annotation-editor";

        var titleElem = document.createElement("h2");
        titleElem.innerHTML = "Create Annotation";
        annotationEditor.appendChild(titleElem);

        var quotationContainer = document.createElement("div");
        annotationEditor.appendChild(quotationContainer);
        quotationContainer.className = "quotation-container";

        var quotation = document.createElement("cite");
        quotationContainer.appendChild(quotation);
        quotation.className = "quotation";
        quotation.innerHTML = quoteText;

        var textArea = document.createElement("textArea");
        textArea.id = "annotationEdit";
        annotationEditor.appendChild(textArea);

        var submitButton = document.createElement("button");
        submitButton.innerHTML = "Submit Annotation";
        annotationEditor.appendChild(submitButton);
        submitButton.onclick = function(){
            var annotation = {
                "caseId": caseId,
                "authorId": authorId,
                "quotationId": quotationId,
                "text": textArea.value,
                "id": 0
            };

            //Send our annotation to the server
            var xhr = new XMLHttpRequest();
            xhr.open("POST", '/putannotation', true);
            //Send the proper header information along with the request
            xhr.setRequestHeader("Content-type", "application/json");
            xhr.onreadystatechange = function() {//Call a function when the state changes.
                if(xhr.readyState == XMLHttpRequest.DONE && xhr.status == 200) {
                    convertEditorToAnnotation(xhr.response, textArea.value);
                }
            }
            xhr.send(JSON.stringify(annotation));
        };

        $('#annotations').prepend(annotationEditor);

        // Show/Hide Tabs
        $('#tab-container #annotations-container').show().siblings().hide();
        // Change/remove current tab to active
        $('#tabs #annotations-container').parent('li').addClass('active').siblings().removeClass('active');

    }

    function convertEditorToAnnotation(annotationId, annotationText){
        //TODO: Author name hardcoded!
        var authorName = "Tom Jones";
        var editor = document.getElementById("annotation-editor");
        editor.id = "annotation" + annotationId;
        editor.firstChild.innerHTML = "Written by " + authorName;
        //Remove button
        editor.removeChild(editor.lastChild);
        //Remove textarea
        editor.removeChild(editor.lastChild);

        var p = document.createElement("p");
        p.innerHTML = annotationText;
        editor.appendChild(p);
    }

    function showAnnotationsTab() {
        var currentAttrValue = jQuery(this).attr('href');

        // Show/Hide Tabs
        $('#tab-container ' + currentAttrValue).show().siblings().hide();

        // Change/remove current tab to active
        //$(this).parent('li').addClass('active').siblings().removeClass('active');

        //e.preventDefault();
        return false;
    }

    // adds styling and links to all quotations
    function updateStyling() {
        // clear existing highlights
        highlighter.removeAllHighlights();

        // convert all quotations into CharacterRange objects
        for (var i = 0; i < quotations.length; i++) {
            var quo = quotations[i];
            // create a new range encompassing the quotation text and apply it to the selection
            var range = rangy.createRange();
            //var linktext = "quotations#" + quo.startIndex + "-" + quo.endIndex;
            var linktext = "#annotations-container";
            var taglength = 4;
            var offset = linktext.length - taglength;
            range.selectCharacters(document.getElementById('case'), quo.startIndex + offset, quo.endIndex + offset);

            // from the range we can convert to a CharacterRange
            var converter = highlighter.converter;
            var charRange =  converter.rangeToCharacterRange(range, document.getElementById('case'));

            // each quotation is applied one at a time so the correct quotation link can be manually inserted
            var charRanges = [];
            charRanges.push(charRange);
            console.log("char range");
            console.log(charRange);
            highlighter.highlightCharacterRanges("quotation", charRanges);

            // add links manually to the highlights
            var highlights = document.getElementsByClassName("quotation");
            for(var j = 0; j < highlights.length; j++) {
                // so quotations with links aren't overwritten
                if(highlights[j].href !== "") {
                    continue;
                }

                // set the link to be the quotations discussion page with an anchor pointing to the quotation
                //highlights[j].href = "quotations#" + quo.startIndex + "-" + quo.endIndex;
                highlights[j].href = "#annotations-container";
                highlights[j].onclick = showAnnotationsTab;
            }
        }
    }

    // returns true if there exists an quotation with the identical start and end indexes
    function quotationExists(start, end) {
        // check an identical quotation does not already exist
        for (var i = 0; i < quotations.length; i++) {
            // quotation already exists
            if (quotations[i].start === start && quotations[i].end === end) {
                return true;
            }
        }
        return false;
    }

    updateStyling();

    // on releasing the mouse check for selected text
    $(document).click(function (e) {
        handleTextSelection(e);
    });
};