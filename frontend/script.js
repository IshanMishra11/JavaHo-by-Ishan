/**
 * Javaho IDE - Futuristic Glassmorphism Client Application (Enhanced Edition)
 */

document.addEventListener('DOMContentLoaded', () => {
    // DOM Element References
    const runBtn = document.getElementById('runBtn');
    const clearBtn = document.getElementById('clearBtn');
    const samplesBtn = document.getElementById('samplesBtn');
    const samplesMenu = document.getElementById('samplesMenu');
    const outputConsole = document.getElementById('outputConsole');
    const errorConsole = document.getElementById('errorConsole');
    const errorBadge = document.getElementById('errorBadge');
    const statusIndicator = document.getElementById('statusIndicator');
    const statusText = document.getElementById('statusText');
    const statsTime = document.getElementById('statsTime');
    const codeFallback = document.getElementById('codeFallback');
    const resizer = document.getElementById('resizer');
    const editorSection = document.querySelector('.editor-section');
    const consoleSection = document.querySelector('.console-section');
    const terminalWelcome = document.querySelector('.terminal-welcome');
    const astTreeView = document.getElementById('astTreeView');
    const tokenList = document.getElementById('tokenList');
    const copyOutputBtn = document.getElementById('copyOutputBtn');
    const formatBtn = document.getElementById('formatBtn');
    const aboutDevBtn = document.getElementById('aboutDevBtn');
    const devModalOverlay = document.getElementById('devModalOverlay');
    const modalCloseBtn = document.getElementById('modalCloseBtn');
    
    let editor = null;

    // Live API Endpoint Configuration
    // Automatically uses relative /api/run on localhost, or Render production URL on remote deployment
    const RENDER_BACKEND_URL = 'https://javaho-backend.onrender.com';
    const API_ENDPOINT = (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1')
        ? '/api/run'
        : `${RENDER_BACKEND_URL}/api/run`;

    // Code Samples Library
    const codeSamples = {
        fullDemo: `# Javaho Language Demo

rakh naam = "Ishan"
rakh age = 21

dikha "Ram Ram!"
dikha naam

agar age >= 18
    dikha "Adult"
nahin
    dikha "Minor"
khatam

rakh i = 1

jabtak i <= 3
    dikha i
    i = i + 1
khatam`,

        arithmetic: `# Math & Operations in Javaho

rakh a = 10
rakh b = 5

dikha "a + b = " + (a + b)
dikha "a - b = " + (a - b)
dikha "a * b = " + (a * b)
dikha "a / b = " + (a / b)

agar a > b
    dikha "a is greater than b"
khatam`,

        whileLoop: `# While Loop (jabtak)

rakh count = 1

jabtak count <= 5
    dikha "Count is: " + count
    count = count + 1
khatam`,

        conditionals: `# Conditionals (agar / nahin)

rakh marks = 85

agar marks >= 90
    dikha "Grade: A+"
nahin
    agar marks >= 80
        dikha "Grade: A"
    nahin
        dikha "Grade: B"
    khatam
khatam`
    };

    // Initialize Monaco Editor with Custom Neon Dark Theme
    if (typeof require !== 'undefined') {
        require.config({ paths: { 'vs': 'https://cdnjs.cloudflare.com/ajax/libs/monaco-editor/0.39.0/min/vs' }});
        require(['vs/editor/editor.main'], function() {
            monaco.languages.register({ id: 'javaho' });

            monaco.languages.setMonarchTokensProvider('javaho', {
                keywords: ['rakh', 'dikha', 'agar', 'nahin', 'jabtak', 'khatam', 'sahi', 'galat'],
                operators: ['=', '+', '-', '*', '/', '==', '!=', '<', '>', '<=', '>='],
                tokenizer: {
                    root: [
                        [/#.*/, 'comment'],
                        [/"([^"\\]|\\.)*"/, 'string'],
                        [/\b\d+\b/, 'number'],
                        [/[a-zA-Z_][a-zA-Z0-9_]*/, {
                            cases: {
                                '@keywords': 'keyword',
                                '@default': 'identifier'
                            }
                        }],
                        [/[=+\-*/<>!]+/, 'operator']
                    ]
                }
            });

            // Autocomplete snippets
            monaco.languages.registerCompletionItemProvider('javaho', {
                provideCompletionItems: () => {
                    const suggestions = [
                        { label: 'rakh', kind: monaco.languages.CompletionItemKind.Keyword, insertText: 'rakh ${1:varName} = ${2:value}' },
                        { label: 'dikha', kind: monaco.languages.CompletionItemKind.Keyword, insertText: 'dikha ${1:expression}' },
                        { label: 'agar', kind: monaco.languages.CompletionItemKind.Keyword, insertText: 'agar ${1:condition}\n\tdikha ${2:statement}\nkhatam' },
                        { label: 'nahin', kind: monaco.languages.CompletionItemKind.Keyword, insertText: 'nahin' },
                        { label: 'jabtak', kind: monaco.languages.CompletionItemKind.Keyword, insertText: 'jabtak ${1:condition}\n\t${2:statement}\nkhatam' },
                        { label: 'khatam', kind: monaco.languages.CompletionItemKind.Keyword, insertText: 'khatam' },
                        { label: 'sahi', kind: monaco.languages.CompletionItemKind.Keyword, insertText: 'sahi' },
                        { label: 'galat', kind: monaco.languages.CompletionItemKind.Keyword, insertText: 'galat' }
                    ].map(item => ({
                        ...item,
                        insertTextRules: monaco.languages.CompletionItemInsertTextRule.InsertAsSnippet
                    }));
                    return { suggestions: suggestions };
                }
            });

            // Custom Futuristic Theme Definition
            monaco.editor.defineTheme('javahoCyber', {
                base: 'vs-dark',
                inherit: true,
                rules: [
                    { token: 'keyword', foreground: 'c084fc', fontStyle: 'bold' },
                    { token: 'string', foreground: '38bdf8' },
                    { token: 'number', foreground: 'fbbf24' },
                    { token: 'comment', foreground: '64748b', fontStyle: 'italic' },
                    { token: 'operator', foreground: 'f1f5f9' },
                    { token: 'identifier', foreground: 'f8fafc' }
                ],
                colors: {
                    'editor.background': '#0b111e00',
                    'editor.foreground': '#f8fafc',
                    'editorLineNumber.foreground': '#475569',
                    'editorLineNumber.activeForeground': '#a78bfa',
                    'editorCursor.foreground': '#38bdf8',
                    'editor.selectionBackground': '#3b82f640',
                    'editor.lineHighlightBackground': '#ffffff05'
                }
            });

            editor = monaco.editor.create(document.getElementById('monacoEditor'), {
                value: codeSamples.fullDemo,
                language: 'javaho',
                theme: 'javahoCyber',
                automaticLayout: true,
                fontSize: 14.5,
                fontFamily: "'Fira Code', monospace",
                fontLigatures: true,
                minimap: { enabled: false },
                scrollBeyondLastLine: false,
                lineNumbersMinChars: 3,
                padding: { top: 16, bottom: 16 }
            });

            // Ctrl/Cmd + Enter Key Binding
            editor.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyCode.Enter, () => {
                runProgram();
            });
        });
    } else {
        document.getElementById('monacoEditor').style.display = 'none';
        codeFallback.style.display = 'block';
        codeFallback.value = codeSamples.fullDemo;
    }

    function getCode() {
        return editor ? editor.getValue() : codeFallback.value;
    }

    function setCode(val) {
        if (editor) {
            editor.setValue(val);
        } else {
            codeFallback.value = val;
        }
    }

    // Execution Logic
    async function runProgram() {
        const code = getCode().trim();
        if (!code) return;

        setStatus('executing', 'Executing...');
        const startTime = performance.now();

        try {
            const response = await fetch(API_ENDPOINT, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ code: code })
            });

            const endTime = performance.now();
            const duration = Math.round(endTime - startTime);
            statsTime.textContent = `${duration} ms`;

            if (!response.ok) {
                throw new Error(`HTTP ${response.status}`);
            }

            const data = await response.json();

            if (data.success) {
                setStatus('ready', 'Ready');
                updateConsoleOutput(data.output);
                updateAstInspector(data.astTree, data.tokens);
                clearErrors();
            } else {
                setStatus('error', 'Execution Error');
                displayErrors(data.errors);
            }
        } catch (err) {
            setStatus('error', 'Server Error');
            displayErrors([`Connection Failed: ${err.message}. Ensure backend API is active at ${API_ENDPOINT}`]);
        }
    }

    function updateConsoleOutput(text) {
        terminalWelcome.style.display = 'none';
        outputConsole.style.display = 'block';
        outputConsole.innerHTML = '';

        if (!text || text.trim() === '') {
            outputConsole.innerHTML = '<span class="text-muted">(Program completed with no output)</span>';
            return;
        }

        const lines = text.split('\n');
        lines.forEach(line => {
            const lineEl = document.createElement('div');
            lineEl.className = 'log-line';
            
            const symbol = document.createElement('span');
            symbol.className = 'prompt-symbol';
            symbol.textContent = '>';
            
            const textNode = document.createTextNode(line);
            
            lineEl.appendChild(symbol);
            lineEl.appendChild(textNode);
            outputConsole.appendChild(lineEl);
        });

        switchTab('outputTab');
    }

    function updateAstInspector(astTree, tokens) {
        if (astTreeView) {
            astTreeView.textContent = astTree || 'No AST generated.';
        }
        if (tokenList) {
            tokenList.innerHTML = '';
            if (tokens && tokens.length > 0) {
                tokens.forEach(tokStr => {
                    const chip = document.createElement('span');
                    chip.className = 'token-chip';
                    chip.textContent = tokStr;
                    tokenList.appendChild(chip);
                });
            } else {
                tokenList.innerHTML = '<span class="text-muted">No tokens available.</span>';
            }
        }
    }

    function displayErrors(errors) {
        errorConsole.innerHTML = '';
        errorBadge.textContent = errors.length;
        errorBadge.classList.add('has-errors');

        errors.forEach(errStr => {
            const card = document.createElement('div');
            card.className = 'error-card';

            const title = document.createElement('div');
            title.className = 'error-header-title';
            title.innerHTML = '<i class="fa-solid fa-triangle-exclamation"></i> Execution Error';

            const msg = document.createElement('div');
            msg.className = 'error-body-text';
            msg.textContent = errStr;

            card.appendChild(title);
            card.appendChild(msg);
            errorConsole.appendChild(card);
        });

        switchTab('errorTab');
    }

    function clearErrors() {
        errorConsole.innerHTML = `
            <div class="empty-state">
                <div class="empty-icon text-emerald"><i class="fa-solid fa-shield-check"></i></div>
                <h3>No Errors Detected</h3>
                <p>Your code compiled & executed cleanly without syntax or runtime issues.</p>
            </div>`;
        errorBadge.textContent = '0';
        errorBadge.classList.remove('has-errors');
    }

    function setStatus(state, text) {
        const dot = statusIndicator.querySelector('.pulse-dot');
        dot.className = 'pulse-dot ' + state;
        statusText.textContent = text;
    }

    // Tabs Navigation
    const tabBtns = document.querySelectorAll('.tab-btn');
    const tabPanes = document.querySelectorAll('.tab-pane');

    function switchTab(tabId) {
        tabBtns.forEach(btn => {
            btn.classList.toggle('active', btn.dataset.tab === tabId);
        });
        tabPanes.forEach(pane => {
            pane.classList.toggle('active', pane.id === tabId);
        });
    }

    tabBtns.forEach(btn => {
        btn.addEventListener('click', () => switchTab(btn.dataset.tab));
    });

    // Sample Code Dropdown
    samplesBtn.addEventListener('click', (e) => {
        e.stopPropagation();
        samplesMenu.classList.toggle('show');
    });

    document.addEventListener('click', () => samplesMenu.classList.remove('show'));

    samplesMenu.querySelectorAll('a').forEach(item => {
        item.addEventListener('click', (e) => {
            e.preventDefault();
            const key = item.dataset.sample;
            if (codeSamples[key]) {
                setCode(codeSamples[key]);
            }
            samplesMenu.classList.remove('show');
        });
    });

    // Copy Output Action
    if (copyOutputBtn) {
        copyOutputBtn.addEventListener('click', () => {
            const text = outputConsole.innerText || '';
            if (text) {
                navigator.clipboard.writeText(text);
                const originalIcon = copyOutputBtn.innerHTML;
                copyOutputBtn.innerHTML = '<i class="fa-solid fa-check text-emerald"></i>';
                setTimeout(() => copyOutputBtn.innerHTML = originalIcon, 1500);
            }
        });
    }

    // Format Code Action
    if (formatBtn) {
        formatBtn.addEventListener('click', () => {
            if (editor) {
                editor.getAction('editor.action.formatDocument')?.run();
            }
        });
    }

    // Developer Modal Overlay Handlers
    if (aboutDevBtn && devModalOverlay) {
        aboutDevBtn.addEventListener('click', () => {
            devModalOverlay.classList.add('show');
        });

        modalCloseBtn.addEventListener('click', () => {
            devModalOverlay.classList.remove('show');
        });

        devModalOverlay.addEventListener('click', (e) => {
            if (e.target === devModalOverlay) {
                devModalOverlay.classList.remove('show');
            }
        });

        document.addEventListener('keydown', (e) => {
            if (e.key === 'Escape' && devModalOverlay.classList.contains('show')) {
                devModalOverlay.classList.remove('show');
            }
        });
    }

    // Button Listeners
    runBtn.addEventListener('click', runProgram);
    clearBtn.addEventListener('click', () => {
        outputConsole.style.display = 'none';
        terminalWelcome.style.display = 'flex';
        clearErrors();
        statsTime.textContent = '--';
        if (astTreeView) astTreeView.textContent = 'Run Javaho code to visualize the generated AST tree structure.';
        if (tokenList) tokenList.innerHTML = '<span class="text-muted">No tokens scanned yet.</span>';
    });

    // Quick Token Chips Click Insertion
    document.querySelectorAll('.kw-chip').forEach(chip => {
        chip.addEventListener('click', () => {
            const word = chip.textContent;
            if (editor) {
                editor.trigger('keyboard', 'type', { text: word + ' ' });
                editor.focus();
            }
        });
    });

    // Resizer Dragging Handler
    let isDragging = false;
    resizer.addEventListener('mousedown', () => {
        isDragging = true;
        resizer.classList.add('dragging');
        document.body.style.cursor = 'col-resize';
    });

    document.addEventListener('mousemove', (e) => {
        if (!isDragging) return;
        const containerWidth = document.querySelector('.ide-container').clientWidth;
        const newLeftWidth = e.clientX - 24;
        const leftPercent = (newLeftWidth / containerWidth) * 100;
        
        if (leftPercent > 20 && leftPercent < 80) {
            editorSection.style.flex = leftPercent;
            consoleSection.style.flex = 100 - leftPercent;
        }
    });

    document.addEventListener('mouseup', () => {
        if (isDragging) {
            isDragging = false;
            resizer.classList.remove('dragging');
            document.body.style.cursor = 'default';
        }
    });
});
