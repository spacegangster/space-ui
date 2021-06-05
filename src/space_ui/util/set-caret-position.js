
function setCaretPosition__contenteditable(elem, caretPos) {
    const range = document.createRange();
    const textNode = elem.childNodes[0]
    const focusNode = textNode || elem
    range.setStart(focusNode, caretPos)
    range.setEnd(focusNode, caretPos)
    const sel = window.getSelection();
    sel.removeAllRanges();
    sel.addRange(range);
}

export function setCaretPosition(elem, caretPos) {
    if (elem == null) { return }
    //
    if (elem.createTextRange) {
        var range = elem.createTextRange()
        range.move('character', caretPos)
        range.select()
    }
    else if (elem.selectionStart) {
        elem.focus()
        elem.setSelectionRange(caretPos, caretPos)
    } else {
        setCaretPosition__contenteditable(elem, caretPos)
    }
}

window.scp = setCaretPosition
